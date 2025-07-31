package com.myfoodcafe.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EnquiryRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotBlank(message = "Please select a category")
    private String category;

    @NotBlank(message = "Message is required")
    @Size(min = 10, message = "Message must be at least 10 characters long")
    private String message;
}