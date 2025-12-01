package com.CSO2.product_catalogue_service.service;

import com.CSO2.product_catalogue_service.dto.request.ReviewSubmitRequest;
import com.CSO2.product_catalogue_service.model.Product;
import com.CSO2.product_catalogue_service.model.Review;
import com.CSO2.product_catalogue_service.repository.ProductRepository;
import com.CSO2.product_catalogue_service.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ReviewService reviewService;

    @Test
    void addReview_ShouldSaveReviewAndUpdateProductStats() {
        String productId = "1";
        ReviewSubmitRequest request = new ReviewSubmitRequest();
        request.setRating(5);
        request.setComment("Great product!");

        Product product = new Product();
        product.setId(productId);
        product.setAverageRating(4.0);
        product.setReviewCount(1);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(reviewRepository.save(any(Review.class))).thenReturn(new Review());

        reviewService.addReview(productId, request);

        verify(reviewRepository).save(any(Review.class));
        verify(productRepository).save(product);

        assertEquals(2, product.getReviewCount());
        assertEquals(4.5, product.getAverageRating());
    }
}
