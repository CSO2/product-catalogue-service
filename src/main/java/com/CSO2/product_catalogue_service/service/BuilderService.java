package com.CSO2.product_catalogue_service.service;

import com.CSO2.product_catalogue_service.dto.response.BuilderSuggestionDTO;
import com.CSO2.product_catalogue_service.model.Product;
import com.CSO2.product_catalogue_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BuilderService {

    private final ProductRepository productRepository;
    private final MongoTemplate mongoTemplate;

    public List<String> checkCompatibility(List<String> productIds) {
        List<Product> products = (List<Product>) productRepository.findAllById(productIds);
        List<String> issues = new ArrayList<>();

        Product cpu = findProductByAttributeKey(products, "socket"); // Assuming CPU has socket
        Product motherboard = findProductByAttributeKey(products, "formFactor"); // Assuming Mobo has formFactor
        // Note: This identification logic is simplified. In reality, we'd check
        // Category.

        // Better identification by Category Name or ID (assuming we know them or fetch
        // them)
        // For now, let's try to identify by attributes present.

        // 1. Check CPU Socket vs Mobo Socket
        if (cpu != null && motherboard != null) {
            String cpuSocket = cpu.getAttributes().get("socket");
            String moboSocket = motherboard.getAttributes().get("socket");
            if (cpuSocket != null && moboSocket != null && !cpuSocket.equalsIgnoreCase(moboSocket)) {
                issues.add("Incompatible Socket: CPU is " + cpuSocket + " but Motherboard is " + moboSocket);
            }
        }

        // 2. Check PSU Wattage > Total TDP
        double totalTdp = 0;
        Product psu = null;

        for (Product p : products) {
            if (p.getAttributes() != null && p.getAttributes().containsKey("tdp")) {
                String tdpStr = p.getAttributes().get("tdp").replace("W", "").trim();
                try {
                    totalTdp += Double.parseDouble(tdpStr);
                } catch (NumberFormatException e) {
                    // ignore
                }
            }
            if (p.getAttributes() != null && p.getAttributes().containsKey("wattage")) {
                psu = p;
            }
        }

        if (psu != null) {
            String wattageStr = psu.getAttributes().get("wattage").replace("W", "").trim();
            try {
                double psuWattage = Double.parseDouble(wattageStr);
                if (psuWattage < totalTdp) {
                    issues.add("Insufficient Power: Total TDP is " + totalTdp + "W but PSU is " + psuWattage + "W");
                }
            } catch (NumberFormatException e) {
                // ignore
            }
        }

        return issues;
    }

    public BuilderSuggestionDTO getSuggestions(String cpuId) {
        Product cpu = productRepository.findById(cpuId).orElseThrow(() -> new RuntimeException("CPU not found"));
        String socket = cpu.getAttributes().get("socket");

        BuilderSuggestionDTO suggestions = new BuilderSuggestionDTO();

        if (socket != null) {
            // Find compatible Motherboards
            Query moboQuery = new Query();
            moboQuery.addCriteria(Criteria.where("attributes.socket").is(socket));
            // Add category filter for Motherboard if we had the ID. For now assume
            // attribute check is enough or we'd need category IDs.
            // Let's assume we filter by category name "Motherboard" if we could, but we
            // don't have it handy.
            // We'll rely on the attribute "socket" being present primarily on Mobos (and
            // CPUs, so exclude self).
            moboQuery.addCriteria(Criteria.where("id").ne(cpuId));

            List<Product> mobos = mongoTemplate.find(moboQuery, Product.class);
            suggestions.setCompatibleMotherboards(mobos);

            // Find compatible RAM
            // Assuming CPU/Mobo implies DDR version.
            String memoryType = cpu.getAttributes().get("memoryType"); // e.g. "DDR5"
            if (memoryType != null) {
                Query ramQuery = new Query();
                ramQuery.addCriteria(Criteria.where("attributes.memoryType").is(memoryType));
                List<Product> rams = mongoTemplate.find(ramQuery, Product.class);
                suggestions.setCompatibleRam(rams);
            }
        }

        return suggestions;
    }

    private Product findProductByAttributeKey(List<Product> products, String key) {
        return products.stream()
                .filter(p -> p.getAttributes() != null && p.getAttributes().containsKey(key))
                .findFirst()
                .orElse(null);
    }
}
