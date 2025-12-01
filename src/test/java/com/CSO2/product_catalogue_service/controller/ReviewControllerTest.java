package com.CSO2.product_catalogue_service.controller;

import com.CSO2.product_catalogue_service.dto.request.ReviewSubmitRequest;
import com.CSO2.product_catalogue_service.model.Review;
import com.CSO2.product_catalogue_service.repository.ReviewRepository;
import com.CSO2.product_catalogue_service.service.ReviewService;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ReviewControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ReviewService reviewService;

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewController reviewController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(reviewController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setMessageConverters(new Jackson3HttpMessageConverter(objectMapper))
                .build();
    }

    @Test
    void addReview_ShouldReturnOk() throws Exception {
        ReviewSubmitRequest request = new ReviewSubmitRequest();
        request.setRating(5);
        request.setComment("Great product!");

        doNothing().when(reviewService).addReview(anyString(), any(ReviewSubmitRequest.class));

        mockMvc.perform(post("/api/products/1/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void getReviews_ShouldReturnPageOfReviews() throws Exception {
        Review review = new Review();
        review.setId("1");
        review.setProductId("1");
        review.setComment("Great product!");

        List<Review> content = List.of(review);
        Page<Review> page = new PageImpl<>(content, PageRequest.of(0, 10), content.size());

        when(reviewRepository.findByProductId(anyString(), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/products/1/reviews"))
                .andExpect(status().isOk());
    }

    static class Jackson3HttpMessageConverter extends AbstractHttpMessageConverter<Object> {
        private final ObjectMapper objectMapper;

        public Jackson3HttpMessageConverter(ObjectMapper objectMapper) {
            super(MediaType.APPLICATION_JSON);
            this.objectMapper = objectMapper;
        }

        @Override
        protected boolean supports(Class<?> clazz) {
            return true;
        }

        @Override
        protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage)
                throws IOException, HttpMessageNotReadableException {
            return objectMapper.readValue(inputMessage.getBody(), clazz);
        }

        @Override
        protected void writeInternal(Object o, HttpOutputMessage outputMessage)
                throws IOException, HttpMessageNotWritableException {
            objectMapper.writeValue(outputMessage.getBody(), o);
        }
    }
}
