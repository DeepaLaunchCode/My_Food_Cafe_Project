package com.myfoodcafe.repository;

import com.myfoodcafe.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ReviewRepository extends JpaRepository<Review, Long> {}
