package com.CSO2.product_catalogue_service.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductListDTO {
    private String id;
    private String name;
    private BigDecimal price;
    private String imageUrl;
    private Double rating;
}
