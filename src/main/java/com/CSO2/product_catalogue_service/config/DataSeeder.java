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

            // Seed Components for PC Builder
            Product cpu = new Product();
            cpu.setName("Intel Core i9-13900K");
            cpu.setSlug("intel-core-i9-13900k");
            cpu.setDescription("24 cores (8 P-cores + 16 E-cores) and 32 threads.");
            cpu.setPrice(new BigDecimal("589.99"));
            cpu.setStockLevel(20);
            cpu.setBrand("Intel");
            cpu.setCategoryId("components"); // Assuming 'components' is the ID for Components category
            cpu.setSubcategory("CPU");
            cpu.setImageUrls(Arrays.asList("https://m.media-amazon.com/images/I/61S7A7iQ-0L._AC_SL1500_.jpg"));
            cpu.setIsActive(true);
            cpu.setAverageRating(4.8);
            cpu.setReviewCount(150);
            Map<String, String> cpuAttrs = new HashMap<>();
            cpuAttrs.put("socketType", "LGA1700");
            cpuAttrs.put("tdp", "125");
            cpu.setAttributes(cpuAttrs);

            Product mobo = new Product();
            mobo.setName("ASUS ROG Maximus Z790 Hero");
            mobo.setSlug("asus-rog-maximus-z790-hero");
            mobo.setDescription("Intel Z790 ATX motherboard with 20+1 power stages.");
            mobo.setPrice(new BigDecimal("629.99"));
            mobo.setStockLevel(15);
            mobo.setBrand("ASUS");
            mobo.setCategoryId("components");
            mobo.setSubcategory("Motherboard");
            mobo.setImageUrls(Arrays.asList("https://m.media-amazon.com/images/I/81p-Yq9xYAL._AC_SL1500_.jpg"));
            mobo.setIsActive(true);
            mobo.setAverageRating(4.7);
            mobo.setReviewCount(85);
            Map<String, String> moboAttrs = new HashMap<>();
            moboAttrs.put("socketType", "LGA1700");
            moboAttrs.put("formFactor", "ATX");
            moboAttrs.put("memoryType", "DDR5");
            mobo.setAttributes(moboAttrs);

            Product gpu = new Product();
            gpu.setName("NVIDIA GeForce RTX 4090");
            gpu.setSlug("nvidia-geforce-rtx-4090");
            gpu.setDescription("The ultimate GeForce GPU.");
            gpu.setPrice(new BigDecimal("1599.99"));
            gpu.setStockLevel(5);
            gpu.setBrand("NVIDIA");
            gpu.setCategoryId("components");
            gpu.setSubcategory("GPU");
            gpu.setImageUrls(Arrays.asList("https://m.media-amazon.com/images/I/61Zt+8-XwLL._AC_SL1500_.jpg"));
            gpu.setIsActive(true);
            gpu.setAverageRating(4.9);
            gpu.setReviewCount(200);
            Map<String, String> gpuAttrs = new HashMap<>();
            gpuAttrs.put("powerRequirement", "450");
            gpu.setAttributes(gpuAttrs);

            productRepository.saveAll(Arrays.asList(p1, p2, cpu, mobo, gpu));
            System.out.println("Product data seeded!");
        }
    }
}
