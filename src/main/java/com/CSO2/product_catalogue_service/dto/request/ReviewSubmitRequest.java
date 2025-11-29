package com.CSO2.product_catalogue_service.dto.request;

import lombok.Data;

@Data
public class ReviewSubmitRequest {
    private Integer rating;
    private String comment;
}
