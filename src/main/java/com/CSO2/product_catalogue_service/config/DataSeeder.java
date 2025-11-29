package com.CSO2.product_catalogue_service.config;

import com.CSO2.product_catalogue_service.model.Product;
import com.CSO2.product_catalogue_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        if (productRepository.count() == 0) {
            Product p1 = new Product();
            p1.setName("Gaming Laptop X1");
            p1.setSlug("gaming-laptop-x1");
            p1.setDescription("High performance gaming laptop");
            p1.setPrice(new BigDecimal("1500.00"));
            p1.setStockLevel(10);
            p1.setBrand("TechBrand");
            p1.setCategoryId("laptops");
            p1.setImageUrls(Arrays.asList("http://example.com/laptop.jpg"));
            p1.setIsActive(true);
            p1.setAverageRating(4.5);
            p1.setReviewCount(10);
            Map<String, String> attrs1 = new HashMap<>();
            attrs1.put("CPU", "Intel i9");
            attrs1.put("RAM", "32GB");
            p1.setAttributes(attrs1);

            Product p2 = new Product();
            p2.setName("Wireless Mouse M2");
            p2.setSlug("wireless-mouse-m2");
            p2.setDescription("Ergonomic wireless mouse");
            p2.setPrice(new BigDecimal("50.00"));
            p2.setStockLevel(50);
            p2.setBrand("TechBrand");
            p2.setCategoryId("accessories");
            p2.setImageUrls(Arrays.asList("http://example.com/mouse.jpg"));
            p2.setIsActive(true);
            p2.setAverageRating(4.0);
            p2.setReviewCount(5);
            Map<String, String> attrs2 = new HashMap<>();
            attrs2.put("DPI", "16000");
            p2.setAttributes(attrs2);

            productRepository.saveAll(Arrays.asList(p1, p2));
            System.out.println("Product data seeded!");
        }
    }
}
