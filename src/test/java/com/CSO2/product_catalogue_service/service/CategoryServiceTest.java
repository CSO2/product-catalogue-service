package com.CSO2.product_catalogue_service.service;

import com.CSO2.product_catalogue_service.model.Category;
import com.CSO2.product_catalogue_service.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void getAllCategories_ShouldReturnListOfCategories() {
        Category category = new Category();
        category.setId("1");
        category.setName("Electronics");

        when(categoryRepository.findAll()).thenReturn(List.of(category));

        List<Category> result = categoryService.getAllCategories();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Electronics", result.get(0).getName());
    }

    @Test
    void createCategory_ShouldSaveAndReturnCategory() {
        Category category = new Category();
        category.setName("Electronics");

        Category savedCategory = new Category();
        savedCategory.setId("1");
        savedCategory.setName("Electronics");
        savedCategory.setSlug("electronics");

        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);

        Category result = categoryService.createCategory(category);

        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("electronics", result.getSlug());
    }
}
