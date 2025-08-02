package com.myfoodcafe.repository;

import com.myfoodcafe.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // Method to find a customer by their unique email
    Optional<Customer> findByEmail(String email);
}