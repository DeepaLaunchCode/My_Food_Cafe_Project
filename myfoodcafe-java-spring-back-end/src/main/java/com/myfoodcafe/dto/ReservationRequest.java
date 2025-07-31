package com.myfoodcafe.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ReservationRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be 10 digits")
    private String phone;

    @NotNull(message = "Date is required")
    @FutureOrPresent(message = "Reservation date must be today or in the future")
    private LocalDate reservationDate;

    @NotNull(message = "Time is required")
    private LocalTime reservationTime;

    @Min(value = 1, message = "Number of guests must be at least 1")
    @Max(value = 20, message = "Number of guests cannot exceed 20")
    private int numberOfGuests;
}