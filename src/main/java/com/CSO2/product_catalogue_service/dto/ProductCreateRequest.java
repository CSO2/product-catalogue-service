package com.CSO2.product_catalogue_service.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class ProductCreateRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stockLevel;
    private String brand;
    private String categoryId;
    private List<String> imageUrls;
    private Map<String, String> attributes;
}
