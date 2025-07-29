package com.myfoodcafe.repository;

import com.myfoodcafe.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ReservationRepository extends JpaRepository<Reservation, Long> {}
