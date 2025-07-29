package com.myfoodcafe.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String recipientPhoneNumber;
    private String message;
    private LocalDateTime sentAt;
    private String status; // e.g., "SUCCESS", "FAILED"
    private String twilioMessageId;
}