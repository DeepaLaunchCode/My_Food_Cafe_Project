package com.myfoodcafe.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ReservationResponseDTO {
    private Long id;
    private LocalDate reservationDate;
    private LocalTime reservationTime;
    private int numberOfGuests;
    private CustomerDTO customer; // Nested DTO for customer details
}