package com.myfoodcafe.controller;

import com.myfoodcafe.dto.GenericResponse;
import com.myfoodcafe.dto.ReservationRequest;
import com.myfoodcafe.dto.ReservationResponseDTO;
import com.myfoodcafe.entity.Reservation;
import com.myfoodcafe.service.ReservationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/reservation")
@Tag(name = "Reservation", description = "Endpoints for table reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping
    public ResponseEntity<GenericResponse> createReservation(@Valid @RequestBody ReservationRequest request) {
        Reservation newReservation = reservationService.createReservation(request);
        // It's often better to return the DTO here as well for consistency
        return ResponseEntity.ok(new GenericResponse(true, "Reservation created successfully!", newReservation.getId()));
    }

    // New: Get Reservation by ID
    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse> getReservationById(@PathVariable Long id) {
        // CORRECTED: Removed the line that caused the build error.
        Optional<ReservationResponseDTO> reservationDto = reservationService.getReservationById(id);

        return reservationDto
                .map(dto -> ResponseEntity.ok(new GenericResponse(true, "Reservation found.", dto)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new GenericResponse(false, "Reservation not found with ID: " + id, null)));
    }

    // New: Update Reservation by ID
    @PutMapping("/{id}")
    public ResponseEntity<GenericResponse> updateReservation(@PathVariable Long id, @Valid @RequestBody ReservationRequest request) {
        try {
            Reservation updatedReservation = reservationService.updateReservation(id, request);
            return ResponseEntity.ok(new GenericResponse(true, "Reservation updated successfully!", updatedReservation.getId()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new GenericResponse(false, e.getMessage(), null));
        }
    }

    // New: Delete Reservation by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse> deleteReservation(@PathVariable Long id) {
        try {
            reservationService.deleteReservation(id);
            return ResponseEntity.ok(new GenericResponse(true, "Reservation deleted successfully!", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new GenericResponse(false, e.getMessage(), null));
        }
    }

    // New: Get Reservations by Email (for a customer to view their reservations)
    @GetMapping("/customer/{email}")
    public ResponseEntity<GenericResponse> getReservationsByEmail(@PathVariable String email) {
        // This endpoint returns a List<ReservationResponseDTO> from the service.
        // The GenericResponse handles it correctly as an Object.
        List<ReservationResponseDTO> reservations = reservationService.getReservationsByCustomerEmail(email);
        if (reservations.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new GenericResponse(false, "No reservations found for email: " + email, null));
        }
        return ResponseEntity.ok(new GenericResponse(true, "Reservations found!", reservations));
    }
}