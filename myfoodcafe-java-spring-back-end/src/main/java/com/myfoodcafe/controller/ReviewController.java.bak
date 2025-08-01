package com.myfoodcafe.controller;

import com.myfoodcafe.dto.GenericResponse;
import com.myfoodcafe.dto.ReviewRequest;
import com.myfoodcafe.entity.Review;
import com.myfoodcafe.service.ReviewService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@Tag(name = "Reviews", description = "Endpoints for customer reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping
    public List<Review> getReviews() {
        return reviewService.getAllReviews();
    }

    @PostMapping
    public ResponseEntity<GenericResponse> addReview(@Valid @RequestBody ReviewRequest request) {
        Review newReview = reviewService.createReview(request);
        return ResponseEntity.ok(new GenericResponse(true, "Review submitted successfully!", newReview.getId()));
    }
}