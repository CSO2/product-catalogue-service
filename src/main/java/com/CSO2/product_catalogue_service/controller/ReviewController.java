package com.CSO2.product_catalogue_service.controller;

import com.CSO2.product_catalogue_service.dto.request.ReviewSubmitRequest;
import com.CSO2.product_catalogue_service.model.Review;
import com.CSO2.product_catalogue_service.repository.ReviewRepository;
import com.CSO2.product_catalogue_service.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products/{id}/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewRepository reviewRepository;

    @PostMapping
    public ResponseEntity<Void> addReview(@PathVariable String id, @RequestBody ReviewSubmitRequest req) {
        reviewService.addReview(id, req);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Page<Review>> getReviews(@PathVariable String id, Pageable pageable) {
        return ResponseEntity.ok(reviewRepository.findByProductId(id, pageable));
    }
}
