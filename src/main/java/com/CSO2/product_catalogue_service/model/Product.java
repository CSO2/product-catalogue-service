package com.CSO2.product_catalogue_service.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Document(collection = "products")
public class Product {
    @Id
    private String id;

    @Indexed(unique = true)
    private String slug;

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
    private Boolean isActive;
    private Boolean featured;

    // Deal/Promotion Fields
    private BigDecimal salePrice;
    private Double discountPercentage;
    private java.time.LocalDateTime saleEndDate;
}
