package com.myfoodcafe.service;

import com.myfoodcafe.dto.ReservationRequest;
import com.myfoodcafe.entity.Customer;
import com.myfoodcafe.entity.Reservation;
import com.myfoodcafe.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.format.DateTimeFormatter;
import com.myfoodcafe.service.EmailService;
import com.myfoodcafe.dto.ReservationResponseDTO;
import org.springframework.transaction.annotation.Transactional;

import com.myfoodcafe.dto.CustomerDTO;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private NotificationService notificationService;

    // 1. Inject the EmailService
    @Autowired
    private EmailService emailService;
    @Value("${frontend.url}")
    private String frontendBaseUrl; // Inject the base URL for your frontend application
    // Base URL for your frontend application


    @Transactional(readOnly = true)
    public Optional<ReservationResponseDTO> getReservationById(Long id) {
        return reservationRepository.findById(id)
                .map(this::mapToReservationResponseDTO); // Map the entity to a DTO
    }

    // --- Private helper method to map Entity to DTO ---
    private ReservationResponseDTO mapToReservationResponseDTO(Reservation reservation) {
        // Create and populate the CustomerDTO first
        CustomerDTO customerDTO = new CustomerDTO();
        // This is safe because the transaction is still open here
        customerDTO.setId(reservation.getCustomer().getId());
        customerDTO.setName(reservation.getCustomer().getName());
        customerDTO.setEmail(reservation.getCustomer().getEmail());
        customerDTO.setPhone(reservation.getCustomer().getPhone());

        // Create and populate the main response DTO
        ReservationResponseDTO dto = new ReservationResponseDTO();
        dto.setId(reservation.getId());
        dto.setReservationDate(reservation.getReservationDate());
        dto.setReservationTime(reservation.getReservationTime());
        dto.setNumberOfGuests(reservation.getNumberOfGuests());
        dto.setCustomer(customerDTO); // Set the nested DTO

        return dto;
    }
    public Reservation createReservation(ReservationRequest request) {
        // Find or create the customer
        Customer customer = customerService.findOrCreateCustomer(
                request.getName(),
                request.getEmail(),
                request.getPhone()
        );

        // Create the reservation and link it to the customer
        Reservation reservation = new Reservation();
        reservation.setCustomer(customer); // Link the customer object

        // Set remaining reservation details
        reservation.setReservationDate(request.getReservationDate());
        reservation.setReservationTime(request.getReservationTime());
        reservation.setNumberOfGuests(request.getNumberOfGuests());

        Reservation savedReservation = reservationRepository.save(reservation);

        // --- Date/Time Formatting for Notifications ---
        String formattedDate = savedReservation.getReservationDate().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"));
        String formattedTime = savedReservation.getReservationTime().format(DateTimeFormatter.ofPattern("hh:mm a"));

        // 2. Send SMS Notification with a try-catch block
        try {
            String smsMessage = String.format("Hi %s, your table reservation for %d at MyFoodCafe is confirmed for %s at %s. Your Reservation ID is %d.",
                    customer.getName(), savedReservation.getNumberOfGuests(), formattedDate, formattedTime, savedReservation.getId());
            notificationService.sendSms(customer.getPhone(), smsMessage);
        } catch (Exception e) {
            // Log the error but don't let it crash the main operation
            System.err.println("Error sending reservation SMS: " + e.getMessage());
        }

        // 3. Send Email Notification with a try-catch block
        try {
            String emailSubject = "Your MyFoodCafe Reservation is Confirmed!";
            // Construct the base URL for your frontend application
            //String baseUrl = frontendBaseUrl; // Adjust this to your actual frontend URL
            String cleanBaseUrl = frontendBaseUrl.endsWith("/") ? frontendBaseUrl.substring(0, frontendBaseUrl.length() - 1) : frontendBaseUrl;
            String manageReservationLink = String.format("%s/manage-reservation?id=%d", cleanBaseUrl, savedReservation.getId());


            String emailBody = String.format(
                    "Hello %s,\n\nThis is a confirmation that your table reservation has been successfully made.\n\n" +
                            "Reservation Details:\n" +
                            "Reservation ID: %d\n" +
                            "Date: %s\n" +
                            "Time: %s\n" +
                            "Number of Guests: %d\n\n" +
                            "You can manage your reservation here: %s\n\n" +
                            "We look forward to seeing you!\n\n" +
                            "Sincerely,\nThe MyFoodCafe Team",
                    customer.getName(),
                    savedReservation.getId(),
                    formattedDate,
                    formattedTime,
                    savedReservation.getNumberOfGuests(),
                    manageReservationLink
            );

            emailService.sendSimpleEmail(customer.getEmail(), emailSubject, emailBody);
        } catch (Exception e) {
            // Log the error but don't let it crash the main operation
            System.err.println("Error sending reservation email: " + e.getMessage());
        }

        return savedReservation;
    }

    // New: Get Reservation by ID


    // New: Update Reservation
    public Reservation updateReservation(Long id, ReservationRequest request) {
        Reservation existingReservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found with ID: " + id));

        // Update customer details if they have changed (optional, depending on your business logic)
        Customer customer = existingReservation.getCustomer();
        if (!customer.getName().equals(request.getName()) ||
                !customer.getEmail().equals(request.getEmail()) ||
                !customer.getPhone().equals(request.getPhone())) {
            // If customer details change, you might want to update the existing customer
            // or create a new one, depending on your data model and requirements.
            // For simplicity here, we'll update the existing customer's details.
            customer.setName(request.getName());
            customer.setEmail(request.getEmail());
            customer.setPhone(request.getPhone());
            customerService.saveCustomer(customer); // Assuming a saveCustomer method in CustomerService
        }

        existingReservation.setReservationDate(request.getReservationDate());
        existingReservation.setReservationTime(request.getReservationTime());
        existingReservation.setNumberOfGuests(request.getNumberOfGuests());

        Reservation updatedReservation = reservationRepository.save(existingReservation);

        // Optionally, send update notifications (SMS/Email)
        String formattedDate = updatedReservation.getReservationDate().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"));
        String formattedTime = updatedReservation.getReservationTime().format(DateTimeFormatter.ofPattern("hh:mm a"));

        try {
            String smsMessage = String.format("Hi %s, your reservation ID %d at MyFoodCafe has been updated to %d guests for %s at %s.",
                    customer.getName(), updatedReservation.getId(), updatedReservation.getNumberOfGuests(), formattedDate, formattedTime);
            notificationService.sendSms(customer.getPhone(), smsMessage);
        } catch (Exception e) {
            System.err.println("Error sending reservation update SMS: " + e.getMessage());
        }

        try {
            String emailSubject = "Your MyFoodCafe Reservation Has Been Updated!";
            String emailBody = String.format(
                    "Hello %s,\n\nThis is a confirmation that your table reservation (ID: %d) has been successfully updated.\n\n" +
                            "New Reservation Details:\n" +
                            "Date: %s\n" +
                            "Time: %s\n" +
                            "Number of Guests: %d\n\n" +
                            "We look forward to seeing you!\n\n" +
                            "Sincerely,\nThe MyFoodCafe Team",
                    customer.getName(),
                    updatedReservation.getId(),
                    formattedDate,
                    formattedTime,
                    updatedReservation.getNumberOfGuests()
            );
            emailService.sendSimpleEmail(customer.getEmail(), emailSubject, emailBody);
        } catch (Exception e) {
            System.err.println("Error sending reservation update email: " + e.getMessage());
        }

        return updatedReservation;
    }

    // New: Delete Reservation
    public void deleteReservation(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new RuntimeException("Reservation not found with ID: " + id);
        }
        // Optionally, retrieve reservation details to send a cancellation notification
        Optional<Reservation> reservationToDelete = reservationRepository.findById(id);
        if (reservationToDelete.isPresent()) {
            Reservation reservation = reservationToDelete.get();
            Customer customer = reservation.getCustomer();
            String formattedDate = reservation.getReservationDate().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"));
            String formattedTime = reservation.getReservationTime().format(DateTimeFormatter.ofPattern("hh:mm a"));

            try {
                String smsMessage = String.format("Hi %s, your reservation ID %d for %s at %s has been cancelled.",
                        customer.getName(), reservation.getId(), formattedDate, formattedTime);
                notificationService.sendSms(customer.getPhone(), smsMessage);
            } catch (Exception e) {
                System.err.println("Error sending reservation cancellation SMS: " + e.getMessage());
            }

            try {
                String emailSubject = "Your MyFoodCafe Reservation Has Been Cancelled";
                String emailBody = String.format(
                        "Hello %s,\n\nThis is a confirmation that your table reservation (ID: %d) for %s at %s has been successfully cancelled.\n\n" +
                                "We hope to see you again soon!\n\n" +
                                "Sincerely,\nThe MyFoodCafe Team",
                        customer.getName(),
                        reservation.getId(),
                        formattedDate,
                        formattedTime
                );
                emailService.sendSimpleEmail(customer.getEmail(), emailSubject, emailBody);
            } catch (Exception e) {
                System.err.println("Error sending reservation cancellation email: " + e.getMessage());
            }
        }
        reservationRepository.deleteById(id);
    }
    @Transactional(readOnly = true)
    public List<ReservationResponseDTO> getReservationsByCustomerEmail(String email) {
        return reservationRepository.findByCustomerEmail(email).stream()
                .map(this::mapToReservationResponseDTO)
                .collect(Collectors.toList());
    }}
