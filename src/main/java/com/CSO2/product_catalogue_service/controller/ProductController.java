package com.CSO2.product_catalogue_service.controller;

import com.CSO2.product_catalogue_service.dto.FilterParams;
import com.CSO2.product_catalogue_service.dto.ProductCreateRequest;
import com.CSO2.product_catalogue_service.dto.ProductDetailDTO;
import com.CSO2.product_catalogue_service.dto.ProductListDTO;
import com.CSO2.product_catalogue_service.model.Product;
import com.CSO2.product_catalogue_service.dto.StockCheckItem;
import com.CSO2.product_catalogue_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Page<ProductListDTO>> getProducts(FilterParams params, Pageable pageable) {
        return ResponseEntity.ok(productService.getProducts(params, pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<java.util.List<ProductListDTO>> searchProducts(@RequestParam("q") String query) {
        return ResponseEntity.ok(productService.searchProducts(query));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailDTO> getProductById(@PathVariable String id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping("/{id}/related")
    public ResponseEntity<java.util.List<ProductListDTO>> getRelatedProducts(
            @PathVariable String id,
            @RequestParam(defaultValue = "4") int limit) {
        return ResponseEntity.ok(productService.getRelatedProducts(id, limit));
    }

    // The original @GetMapping for getAllProducts without a path conflicts with the
    // existing one.
    // Assuming it should have a specific path or the existing one is sufficient.
    // For now, I'm adding only the /featured endpoint as explicitly requested in
    // the instruction's title.
    // If getAllProducts was intended, it needs a distinct path or to replace the
    // existing root GET.

    @GetMapping("/featured")
    public List<Product> getFeaturedProducts() {
        return productService.getFeaturedProducts();
    }

    @GetMapping("/low-stock")
    public List<Product> getLowStockProducts(@RequestParam(defaultValue = "10") int threshold) {
        return productService.getLowStockProducts(threshold);
    }

    @GetMapping("/deals")
    public List<Product> getDeals() {
        return productService.getDeals();
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody ProductCreateRequest request) {
        return ResponseEntity.ok(productService.createProduct(request));
    }

    @PostMapping("/validate-stock")
    public ResponseEntity<java.util.List<String>> validateStock(@RequestBody java.util.List<StockCheckItem> items) {
        return ResponseEntity.ok(productService.validateStock(items));
    }

    @PostMapping("/reduce-stock")
    public ResponseEntity<Void> reduceStock(@RequestBody java.util.List<StockCheckItem> items) {
        productService.reduceStock(items);
        return ResponseEntity.ok().build();
    }
}
