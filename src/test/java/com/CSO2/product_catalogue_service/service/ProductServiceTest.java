package com.CSO2.product_catalogue_service.service;

import com.CSO2.product_catalogue_service.dto.request.FilterParams;
import com.CSO2.product_catalogue_service.dto.request.ProductCreateRequest;
import com.CSO2.product_catalogue_service.dto.response.ProductDetailDTO;
import com.CSO2.product_catalogue_service.dto.response.ProductListDTO;
import com.CSO2.product_catalogue_service.model.Category;
import com.CSO2.product_catalogue_service.model.Product;
import com.CSO2.product_catalogue_service.repository.CategoryRepository;
import com.CSO2.product_catalogue_service.repository.ProductRepository;
import com.CSO2.product_catalogue_service.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private ProductService productService;

    @Test
    void getFeaturedProducts_ShouldReturnFeaturedProducts() {
        Product product = new Product();
        product.setFeatured(true);
        when(productRepository.findByFeaturedTrue()).thenReturn(List.of(product));

        List<Product> result = productService.getFeaturedProducts();

        assertFalse(result.isEmpty());
        assertTrue(result.get(0).getFeatured());
    }

    @Test
    void getProductById_ShouldReturnProductDetail() {
        String productId = "1";
        Product product = new Product();
        product.setId(productId);
        product.setName("Test Product");

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(reviewRepository.findByProductId(productId)).thenReturn(Collections.emptyList());

        ProductDetailDTO result = productService.getProductById(productId);

        assertNotNull(result);
        assertEquals(productId, result.getId());
        assertEquals("Test Product", result.getName());
    }

    @Test
    void createProduct_ShouldSaveAndReturnProduct() {
        ProductCreateRequest request = new ProductCreateRequest();
        request.setName("New Product");
        request.setPrice(java.math.BigDecimal.valueOf(100.0));

        Product product = new Product();
        product.setId("1");
        product.setName("New Product");
        product.setPrice(java.math.BigDecimal.valueOf(100.0));

        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = productService.createProduct(request);

        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("New Product", result.getName());
    }

    @Test
    void getProducts_ShouldReturnPageOfProductListDTOs() {
        FilterParams params = new FilterParams();
        Pageable pageable = Pageable.unpaged();
        Product product = new Product();
        product.setId("1");
        product.setName("Test Product");
        product.setCategoryId("cat1");

        Category category = new Category();
        category.setId("cat1");
        category.setName("Electronics");

        when(mongoTemplate.find(any(Query.class), eq(Product.class))).thenReturn(List.of(product));
        when(mongoTemplate.count(any(Query.class), eq(Product.class))).thenReturn(1L);
        when(categoryRepository.findById("cat1")).thenReturn(Optional.of(category));

        Page<ProductListDTO> result = productService.getProducts(params, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Electronics", result.getContent().get(0).getCategory());
    }
}
