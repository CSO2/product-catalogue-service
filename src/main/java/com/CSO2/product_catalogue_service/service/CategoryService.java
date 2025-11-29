package com.CSO2.product_catalogue_service.service;

import com.CSO2.product_catalogue_service.model.Category;
import com.CSO2.product_catalogue_service.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category createCategory(Category category) {
        if (category.getSlug() == null) {
            category.setSlug(category.getName().toLowerCase().replace(" ", "-"));
        }
        return categoryRepository.save(category);
    }
}
