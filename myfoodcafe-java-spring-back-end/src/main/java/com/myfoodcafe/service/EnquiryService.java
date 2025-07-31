package com.myfoodcafe.service;

import com.myfoodcafe.dto.EnquiryRequest;
import com.myfoodcafe.entity.Enquiry;
import com.myfoodcafe.repository.EnquiryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EnquiryService {

    @Autowired
    private EnquiryRepository enquiryRepository;


    @Autowired
    private EmailService emailService;

    @Value("${admin.email}")
    private String adminEmail;

    public Enquiry submitEnquiry(EnquiryRequest request) {
        // 1. Save the enquiry to the database
        Enquiry enquiry = new Enquiry();
        enquiry.setName(request.getName());
        enquiry.setEmail(request.getEmail());
        enquiry.setCategory(request.getCategory());
        enquiry.setMessage(request.getMessage());
        enquiry.setSubmittedAt(LocalDateTime.now());
        Enquiry savedEnquiry = enquiryRepository.save(enquiry);

        // 2. Send email notifications
        sendAdminNotificationEmail(savedEnquiry);
        sendConfirmationEmailToUser(savedEnquiry);

        return savedEnquiry;
    }

    private void sendAdminNotificationEmail(Enquiry enquiry) {
        try {
            String emailSubject = String.format(
                    "New Enquiry [%s] from MyFoodCafe", enquiry.getCategory());
            String emailBody = String.format(
                    "You have received a new enquiry:\n\n" +
                            "Category: %s\n" +
                            "From: %s\n" +
                            "Email: %s\n" +
                            "Message:\n%s",
                    enquiry.getCategory(), enquiry.getName(), enquiry.getEmail(), enquiry.getMessage()
            );
            // Send email using EmailService

            emailService.sendSimpleEmail(adminEmail, emailSubject, emailBody);
        } catch (Exception e) {
            // Log the error but don't let it crash the main operation
            System.err.println("Error sending Enquiry  email to admin: " + e.getMessage());
        }


    }

    private void sendConfirmationEmailToUser(Enquiry enquiry) {
        try {
            String emailSubject = "We have received your enquiry - MyFoodCafe";
            String emailBody = String.format(
                "Hello %s,\n\n" +
                        "Thank you for contacting us! We have received your message and will get back to you as soon as possible.\n\n" +
                        "Best regards,\nThe MyFoodCafe Team",
                enquiry.getName()
            );
        // Send confirmation email to the user
            emailService.sendSimpleEmail(enquiry.getEmail(), emailSubject, emailBody);
    } catch (Exception e) {
        // Log the error but don't let it crash the main operation
        System.err.println("Error sending Enquiry email to user: " + e.getMessage());
    }
    }
}
