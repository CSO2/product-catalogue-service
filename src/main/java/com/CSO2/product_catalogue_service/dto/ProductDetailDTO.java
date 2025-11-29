package com.CSO2.product_catalogue_service.dto;

import com.CSO2.product_catalogue_service.model.Review;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class ProductDetailDTO {
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stockLevel;
    private String brand;
    private String categoryId;
    private String subcategory;
    private List<String> imageUrls;
    private Map<String, String> attributes;
    private Double averageRating;
    private Integer reviewCount;
    private List<Review> reviews;
}
