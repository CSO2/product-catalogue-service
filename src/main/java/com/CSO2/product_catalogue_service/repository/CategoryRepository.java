package com.CSO2.product_catalogue_service.repository;

import com.CSO2.product_catalogue_service.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
    Category findBySlug(String slug);
}
