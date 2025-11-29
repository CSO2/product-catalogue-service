package com.CSO2.product_catalogue_service.controller;

import com.CSO2.product_catalogue_service.dto.BuilderSuggestionDTO;
import com.CSO2.product_catalogue_service.dto.CompatibilityCheckRequest;
import com.CSO2.product_catalogue_service.service.BuilderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/builder")
@RequiredArgsConstructor
public class BuilderController {

    private final BuilderService builderService;

    @PostMapping("/compatibility")
    public ResponseEntity<List<String>> checkCompatibility(@RequestBody CompatibilityCheckRequest request) {
        return ResponseEntity.ok(builderService.checkCompatibility(request.getProductIds()));
    }

    @GetMapping("/suggestions")
    public ResponseEntity<BuilderSuggestionDTO> getSuggestions(@RequestParam String cpuId) {
        return ResponseEntity.ok(builderService.getSuggestions(cpuId));
    }
}
