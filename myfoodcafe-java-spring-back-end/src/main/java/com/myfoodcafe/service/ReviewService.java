package com.myfoodcafe.service;

import com.myfoodcafe.dto.ReviewRequest;
import com.myfoodcafe.entity.Review;
import com.myfoodcafe.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public Review createReview(ReviewRequest request) {
        Review review = new Review();
        review.setName(request.getName());
        review.setMessage(request.getMessage());
        review.setRating(request.getRating());
        review.setImage(request.getImage());
        review.setCreatedAt(LocalDateTime.now());
        return reviewRepository.save(review);
    }
}