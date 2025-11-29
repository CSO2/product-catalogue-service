package com.CSO2.product_catalogue_service.repository;

import com.CSO2.product_catalogue_service.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    Optional<Product> findBySlug(String slug);

    List<Product> findByFeaturedTrue();

    Page<Product> findByCategoryId(String categoryId, Pageable pageable);

    Page<Product> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description,
            Pageable pageable);

    List<Product> findByStockLevelLessThan(int stockLevel);
}
