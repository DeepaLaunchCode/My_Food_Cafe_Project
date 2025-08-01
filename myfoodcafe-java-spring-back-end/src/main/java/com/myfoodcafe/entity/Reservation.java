package com.myfoodcafe.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "reservation")
@Data
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // Or EAGER if you want to load Customer immediately
    @JoinColumn(name = "customer_id", nullable = false)
    @ToString.Exclude // Prevents recursive toString() calls
    private Customer customer;

    private LocalDate reservationDate;
    private LocalTime reservationTime;
    private int numberOfGuests;
}
