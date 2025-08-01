package com.myfoodcafe.service;

import com.myfoodcafe.dto.EnquiryRequest;
import com.myfoodcafe.entity.Enquiry;
import com.myfoodcafe.repository.EnquiryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnquiryServiceTest {

    @Mock
    private EnquiryRepository enquiryRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private EnquiryService enquiryService;

    private EnquiryRequest sampleRequest;
    private Enquiry savedEnquiry;

    @BeforeEach
    void setUp() {
        sampleRequest = new EnquiryRequest();
        sampleRequest.setName("John Smith");
        sampleRequest.setEmail("john.smith@test.com");
        sampleRequest.setCategory("Franchise");
        sampleRequest.setMessage("I would like to know more about franchise opportunities.");

        savedEnquiry = new Enquiry();
        savedEnquiry.setId(1L);
        savedEnquiry.setName(sampleRequest.getName());
        savedEnquiry.setEmail(sampleRequest.getEmail());
        savedEnquiry.setCategory(sampleRequest.getCategory());
        savedEnquiry.setMessage(sampleRequest.getMessage());

        // Manually set the admin email for the test
        ReflectionTestUtils.setField(enquiryService, "adminEmail", "admin@myfoodcafe.com");
    }

    @Test
    @DisplayName("Should save enquiry and send two emails on successful submission")
    void submitEnquiry_Success() {
        // Arrange
        when(enquiryRepository.save(any(Enquiry.class))).thenReturn(savedEnquiry);

        // Act
        Enquiry result = enquiryService.submitEnquiry(sampleRequest);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());

        // Verify that save was called
        verify(enquiryRepository, times(1)).save(any(Enquiry.class));

        // Verify that sendSimpleEmail was called exactly twice
        verify(emailService, times(2)).sendSimpleEmail(anyString(), anyString(), anyString());

        // Use ArgumentCaptor to capture all arguments for each call
        ArgumentCaptor<String> toEmailCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> subjectCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> bodyCaptor = ArgumentCaptor.forClass(String.class);

        verify(emailService, times(2)).sendSimpleEmail(toEmailCaptor.capture(), subjectCaptor.capture(), bodyCaptor.capture());

        List<String> toEmails = toEmailCaptor.getAllValues();
        List<String> subjects = subjectCaptor.getAllValues();

        // Check admin email
        assertEquals("admin@myfoodcafe.com", toEmails.get(0));
        assertEquals("New Enquiry [Franchise] from MyFoodCafe", subjects.get(0));

        // Check user confirmation email
        assertEquals("john.smith@test.com", toEmails.get(1));
        assertEquals("We have received your enquiry - MyFoodCafe", subjects.get(1));
    }

    @Test
    @DisplayName("Should save enquiry even if user confirmation email fails")
    void submitEnquiry_UserEmailFails() {
        // Arrange
        when(enquiryRepository.save(any(Enquiry.class))).thenReturn(savedEnquiry);
        // Make the user email fail, but the admin email succeed
        doNothing().when(emailService).sendSimpleEmail(eq("admin@myfoodcafe.com"), anyString(), anyString());
        doThrow(new RuntimeException("Mail server down")).when(emailService).sendSimpleEmail(eq("john.smith@test.com"), anyString(), anyString());

        // Act
        Enquiry result = enquiryService.submitEnquiry(sampleRequest);

        // Assert
        assertNotNull(result); // Enquiry should still be saved
        assertEquals(1L, result.getId());

        // Verify that an attempt was made to send both emails
        verify(emailService, times(1)).sendSimpleEmail(eq("admin@myfoodcafe.com"), anyString(), anyString());
        verify(emailService, times(1)).sendSimpleEmail(eq("john.smith@test.com"), anyString(), anyString());
    }
}