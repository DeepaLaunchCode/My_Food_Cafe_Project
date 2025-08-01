package com.myfoodcafe.service;

import com.myfoodcafe.dto.ReservationRequest;
import com.myfoodcafe.dto.ReservationResponseDTO;
import com.myfoodcafe.entity.Customer;
import com.myfoodcafe.entity.Reservation;
import com.myfoodcafe.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    // @Mock creates a mock implementation for the class it is annotating.
    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private CustomerService customerService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private EmailService emailService;

    // @InjectMocks creates an instance of the class and injects the mocks that are created with the @Mock annotations into this instance.
    @InjectMocks
    private ReservationService reservationService;

    private Customer sampleCustomer;
    private Reservation sampleReservation;
    private ReservationRequest sampleRequest;

    @BeforeEach
    void setUp() {
        // This method runs before each test, setting up common objects.
        sampleCustomer = new Customer();
        sampleCustomer.setId(1L);
        sampleCustomer.setName("Jane Doe");
        sampleCustomer.setEmail("jane.doe@example.com");
        sampleCustomer.setPhone("1234567890");

        sampleReservation = new Reservation();
        sampleReservation.setId(101L);
        sampleReservation.setCustomer(sampleCustomer);
        sampleReservation.setReservationDate(LocalDate.now().plusDays(5));
        sampleReservation.setReservationTime(LocalTime.of(19, 0));
        sampleReservation.setNumberOfGuests(4);

        sampleRequest = new ReservationRequest();
        sampleRequest.setName("Jane Doe");
        sampleRequest.setEmail("jane.doe@example.com");
        sampleRequest.setPhone("1234567890");
        sampleRequest.setNumberOfGuests(4);
        sampleRequest.setReservationDate(LocalDate.now().plusDays(5));
        sampleRequest.setReservationTime(LocalTime.of(19, 0));

        // Manually set the value for frontendBaseUrl since @Value won't work in a unit test
        ReflectionTestUtils.setField(reservationService, "frontendBaseUrl", "http://localhost:5173");
    }

    @Test
    @DisplayName("Should create reservation and send notifications successfully")
    void createReservation_Success() {
        // Arrange: Define the behavior of our mocks
        when(customerService.findOrCreateCustomer(anyString(), anyString(), anyString())).thenReturn(sampleCustomer);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(sampleReservation);

        // Act: Call the method we are testing
        Reservation result = reservationService.createReservation(sampleRequest);

        // Assert: Verify the outcomes
        assertNotNull(result);
        assertEquals(101L, result.getId());
        assertEquals("Jane Doe", result.getCustomer().getName());

        // Verify that the save method was called exactly once
        verify(reservationRepository, times(1)).save(any(Reservation.class));

        // Verify that both SMS and Email notifications were sent
        verify(notificationService, times(1)).sendSms(eq("1234567890"), anyString());
        verify(emailService, times(1)).sendSimpleEmail(eq("jane.doe@example.com"), anyString(), anyString());
    }

    @Test
    @DisplayName("Should still create reservation even if SMS notification fails")
    void createReservation_SmsFails() {
        // Arrange
        when(customerService.findOrCreateCustomer(anyString(), anyString(), anyString())).thenReturn(sampleCustomer);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(sampleReservation);
        // Simulate an error during SMS sending
        doThrow(new RuntimeException("Twilio service down")).when(notificationService).sendSms(anyString(), anyString());

        // Act
        Reservation result = reservationService.createReservation(sampleRequest);

        // Assert
        assertNotNull(result); // The reservation should still be created
        assertEquals(101L, result.getId());

        // Verify that the email was still sent
        verify(emailService, times(1)).sendSimpleEmail(eq("jane.doe@example.com"), anyString(), anyString());
    }

    @Test
    @DisplayName("Should return reservation DTO when ID is found")
    void getReservationById_Found() {
        // Arrange
        when(reservationRepository.findById(101L)).thenReturn(Optional.of(sampleReservation));

        // Act
        Optional<ReservationResponseDTO> resultDto = reservationService.getReservationById(101L);

        // Assert
        assertTrue(resultDto.isPresent());
        assertEquals(101L, resultDto.get().getId());
        assertEquals("Jane Doe", resultDto.get().getCustomer().getName());
    }

    @Test
    @DisplayName("Should return empty optional when reservation ID is not found")
    void getReservationById_NotFound() {
        // Arrange
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<ReservationResponseDTO> resultDto = reservationService.getReservationById(999L);

        // Assert
        assertFalse(resultDto.isPresent());
    }

    @Test
    @DisplayName("Should create correct manage reservation link in email")
    void createReservation_EmailLinkIsCorrect() {
        // Arrange
        when(customerService.findOrCreateCustomer(anyString(), anyString(), anyString())).thenReturn(sampleCustomer);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(sampleReservation);

        // Use ArgumentCaptor to capture the string argument passed to sendSimpleEmail
        ArgumentCaptor<String> emailBodyCaptor = ArgumentCaptor.forClass(String.class);

        // Act
        reservationService.createReservation(sampleRequest);

        // Assert
        // Verify email was sent and capture the body
        verify(emailService).sendSimpleEmail(anyString(), anyString(), emailBodyCaptor.capture());

        String capturedEmailBody = emailBodyCaptor.getValue();
        String expectedLink = "http://localhost:5173/manage-reservation?id=101";
        assertTrue(capturedEmailBody.contains(expectedLink), "Email body should contain the correct management link");
    }
}