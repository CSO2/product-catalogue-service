package com.CSO2.product_catalogue_service.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductListDTO {
    private String id;
    private String name;
    private BigDecimal price;
    private String imageUrl;
    private Double rating;

    private String category;
    private String subcategory;
    private String brand;
    private Integer stockLevel;
    private String description;

    // Deal fields
    private BigDecimal salePrice;
    private Double discountPercentage;
    private java.time.LocalDateTime saleEndDate;
}
