package com.myfoodcafe.controller;

import com.myfoodcafe.dto.GenericResponse;
import com.myfoodcafe.dto.ReservationRequest;
import com.myfoodcafe.entity.Reservation;
import com.myfoodcafe.service.ReservationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservations")
@Tag(name = "Reservations", description = "Endpoints for table reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping
    public ResponseEntity<GenericResponse> createReservation(@Valid @RequestBody ReservationRequest request) {
        Reservation newReservation = reservationService.createReservation(request);
        return ResponseEntity.ok(new GenericResponse(true, "Reservation created successfully!", newReservation.getId()));
    }
}