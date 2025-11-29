package com.CSO2.product_catalogue_service.service;

import com.CSO2.product_catalogue_service.dto.*;
import com.CSO2.product_catalogue_service.model.Product;
import com.CSO2.product_catalogue_service.model.Review;
import com.CSO2.product_catalogue_service.repository.ProductRepository;
import com.CSO2.product_catalogue_service.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final MongoTemplate mongoTemplate;

    public List<Product> getFeaturedProducts() {
        return productRepository.findByFeaturedTrue();
    }

    public List<Product> getLowStockProducts(int threshold) {
        return productRepository.findByStockLevelLessThan(threshold);
    }

    public List<Product> getDeals() {
        return productRepository.findByDiscountPercentageGreaterThan(0.0);
    }

    public Page<ProductListDTO> getProducts(FilterParams params, Pageable pageable) {
        Query query = new Query().with(pageable);

        if (params.getCategoryId() != null) {
            query.addCriteria(Criteria.where("categoryId").is(params.getCategoryId()));
        }
        if (params.getMinPrice() != null && params.getMaxPrice() != null) {
            query.addCriteria(Criteria.where("price").gte(params.getMinPrice()).lte(params.getMaxPrice()));
        } else if (params.getMinPrice() != null) {
            query.addCriteria(Criteria.where("price").gte(params.getMinPrice()));
        } else if (params.getMaxPrice() != null) {
            query.addCriteria(Criteria.where("price").lte(params.getMaxPrice()));
        }
        if (params.getBrand() != null) {
            query.addCriteria(Criteria.where("brand").is(params.getBrand()));
        }
        if (params.getSearch() != null && !params.getSearch().isEmpty()) {
            query.addCriteria(new Criteria().orOperator(
                    Criteria.where("name").regex(params.getSearch(), "i"),
                    Criteria.where("description").regex(params.getSearch(), "i")));
        }
        if (params.getAttributes() != null) {
            params.getAttributes()
                    .forEach((key, value) -> query.addCriteria(Criteria.where("attributes." + key).is(value)));
        }

        List<Product> products = mongoTemplate.find(query, Product.class);
        long count = mongoTemplate.count(Query.of(query).limit(0).skip(0), Product.class);

        List<ProductListDTO> dtos = products.stream().map(this::mapToListDTO).collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, count);
    }

    public List<ProductListDTO> searchProducts(String query) {
        if (query == null || query.trim().isEmpty()) {
            return List.of();
        }

        Query mongoQuery = new Query();
        mongoQuery.addCriteria(new Criteria().orOperator(
                Criteria.where("name").regex(query, "i"),
                Criteria.where("description").regex(query, "i"),
                Criteria.where("brand").regex(query, "i")));
        mongoQuery.addCriteria(Criteria.where("isActive").is(true));
        mongoQuery.limit(20); // Limit results

        return mongoTemplate.find(mongoQuery, Product.class).stream()
                .map(this::mapToListDTO)
                .collect(Collectors.toList());
    }

    public ProductDetailDTO getProductById(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        List<Review> reviews = reviewRepository.findByProductId(id);

        return mapToDetailDTO(product, reviews);
    }

    @Transactional
    public void updateStock(String id, int quantity) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        int newStock = product.getStockLevel() - quantity;
        if (newStock < 0) {
            throw new RuntimeException("Insufficient stock for product: " + product.getName());
        }
        product.setStockLevel(newStock);
        productRepository.save(product);
    }

    public List<String> validateStock(List<StockCheckItem> items) {
        List<String> outOfStockItems = new java.util.ArrayList<>();
        for (StockCheckItem item : items) {
            Product product = productRepository.findById(item.getProductId()).orElse(null);
            if (product == null || product.getStockLevel() < item.getQuantity()) {
                outOfStockItems.add(item.getProductId());
            }
        }
        return outOfStockItems;
    }

    @Transactional
    public void reduceStock(List<StockCheckItem> items) {
        for (StockCheckItem item : items) {
            updateStock(item.getProductId(), item.getQuantity());
        }
    }

    public List<ProductListDTO> getRelatedProducts(String productId, int limit) {
        // Get the product to find its category
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Find products in the same category, excluding the current product
        Query query = new Query();
        query.addCriteria(Criteria.where("categoryId").is(product.getCategoryId()));
        query.addCriteria(Criteria.where("_id").ne(productId));
        query.addCriteria(Criteria.where("isActive").is(true));
        query.limit(limit);

        List<Product> relatedProducts = mongoTemplate.find(query, Product.class);
        return relatedProducts.stream()
                .map(this::mapToListDTO)
                .collect(Collectors.toList());
    }

    public Product createProduct(ProductCreateRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockLevel(request.getStockLevel());
        product.setBrand(request.getBrand());
        product.setCategoryId(request.getCategoryId());
        product.setImageUrls(request.getImageUrls());
        product.setAttributes(request.getAttributes());
        product.setIsActive(true);
        product.setAverageRating(0.0);
        product.setReviewCount(0);

        // Generate slug from name (simple version)
        product.setSlug(request.getName().toLowerCase().replace(" ", "-"));

        return productRepository.save(product);
    }

    private ProductListDTO mapToListDTO(Product product) {
        ProductListDTO dto = new ProductListDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setImageUrl(
                product.getImageUrls() != null && !product.getImageUrls().isEmpty() ? product.getImageUrls().get(0)
                        : null);
        dto.setRating(product.getAverageRating());
        return dto;
    }

    private ProductDetailDTO mapToDetailDTO(Product product, List<Review> reviews) {
        ProductDetailDTO dto = new ProductDetailDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStockLevel(product.getStockLevel());
        dto.setBrand(product.getBrand());
        dto.setCategoryId(product.getCategoryId());
        dto.setImageUrls(product.getImageUrls());
        dto.setAttributes(product.getAttributes());
        dto.setAverageRating(product.getAverageRating());
        dto.setReviewCount(product.getReviewCount());
        dto.setReviews(reviews);
        return dto;
    }
}
