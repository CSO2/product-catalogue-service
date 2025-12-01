package com.CSO2.product_catalogue_service.service;

import com.CSO2.product_catalogue_service.dto.response.BuilderSuggestionDTO;
import com.CSO2.product_catalogue_service.model.Product;
import com.CSO2.product_catalogue_service.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuilderServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private BuilderService builderService;

    @Test
    void checkCompatibility_ShouldReturnIssues_WhenSocketsDoNotMatch() {
        Product cpu = new Product();
        cpu.setId("1");
        cpu.setAttributes(Map.of("socket", "AM4"));

        Product mobo = new Product();
        mobo.setId("2");
        mobo.setAttributes(Map.of("socket", "LGA1700", "formFactor", "ATX"));

        when(productRepository.findAllById(any())).thenReturn(List.of(cpu, mobo));

        List<String> issues = builderService.checkCompatibility(List.of("1", "2"));

        assertFalse(issues.isEmpty());
        assertTrue(issues.get(0).contains("Incompatible Socket"));
    }

    @Test
    void getSuggestions_ShouldReturnCompatibleMotherboards() {
        String cpuId = "1";
        Product cpu = new Product();
        cpu.setId(cpuId);
        cpu.setAttributes(Map.of("socket", "AM4"));

        Product mobo = new Product();
        mobo.setId("2");
        mobo.setAttributes(Map.of("socket", "AM4"));

        when(productRepository.findById(cpuId)).thenReturn(Optional.of(cpu));
        when(mongoTemplate.find(any(Query.class), eq(Product.class))).thenReturn(List.of(mobo));

        BuilderSuggestionDTO suggestions = builderService.getSuggestions(cpuId);

        assertNotNull(suggestions);
        assertNotNull(suggestions.getCompatibleMotherboards());
        assertFalse(suggestions.getCompatibleMotherboards().isEmpty());
    }
}
