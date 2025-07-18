package com.myfoodcafe.repository;

import com.myfoodcafe.model.TableReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TableReservationRepository extends JpaRepository<TableReservation, Integer> {
    List<TableReservation> findByCustomerId(int customerId);
}