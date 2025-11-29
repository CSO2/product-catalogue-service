package com.CSO2.product_catalogue_service.dto.response;

import com.CSO2.product_catalogue_service.model.Product;
import lombok.Data;
import java.util.List;

@Data
public class BuilderSuggestionDTO {
    private List<Product> compatibleMotherboards;
    private List<Product> compatibleRam;
}
