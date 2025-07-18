package com.myfoodcafe.repository;

import com.myfoodcafe.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByCustomerId(int customerId);
}