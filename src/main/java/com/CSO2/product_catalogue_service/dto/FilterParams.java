package com.CSO2.product_catalogue_service.dto;

import lombok.Data;
import java.util.Map;

@Data
public class FilterParams {
    private String categoryId;
    private Double minPrice;
    private Double maxPrice;
    private String brand;
    private String search;
    private Map<String, String> attributes;
}
