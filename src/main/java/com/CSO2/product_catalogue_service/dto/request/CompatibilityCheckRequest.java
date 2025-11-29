package com.CSO2.product_catalogue_service.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class CompatibilityCheckRequest {
    private List<String> productIds;
}
