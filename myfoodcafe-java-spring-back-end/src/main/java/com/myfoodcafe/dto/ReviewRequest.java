package com.myfoodcafe.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ReviewRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Review message cannot be empty")
    private String message;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot be more than 5")
    private int rating;

    private String image;
}