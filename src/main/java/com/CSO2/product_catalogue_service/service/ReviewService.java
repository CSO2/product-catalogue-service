package com.CSO2.product_catalogue_service.service;

import com.CSO2.product_catalogue_service.dto.request.ReviewSubmitRequest;
import com.CSO2.product_catalogue_service.model.Product;
import com.CSO2.product_catalogue_service.model.Review;
import com.CSO2.product_catalogue_service.repository.ProductRepository;
import com.CSO2.product_catalogue_service.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void addReview(String productId, ReviewSubmitRequest req) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Review review = new Review();
        review.setProductId(productId);
        review.setRating(req.getRating());
        review.setComment(req.getComment());
        review.setCreatedAt(LocalDateTime.now());
        // User details would typically come from SecurityContext
        review.setUserId("demo-user");
        review.setUserName("Demo User");
        review.setIsVerifiedPurchase(true); // Simplified

        reviewRepository.save(review);

        // Update Product stats
        double currentTotal = product.getAverageRating() * product.getReviewCount();
        int newCount = product.getReviewCount() + 1;
        double newAverage = (currentTotal + req.getRating()) / newCount;

        product.setReviewCount(newCount);
        product.setAverageRating(newAverage);
        productRepository.save(product);
    }
}
