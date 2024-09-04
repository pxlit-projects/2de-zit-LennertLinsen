package be.pxl.services.service;

import be.pxl.services.controller.CategoryResponse;
import be.pxl.services.domain.Category;
import be.pxl.services.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    public CategoryServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCategories() {
        // Arrange
        Category category1 = new Category(1L, "Electronics", Arrays.asList());
        Category category2 = new Category(2L, "Books", Arrays.asList());

        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category1, category2));

        // Act
        List<CategoryResponse> categoryResponses = categoryService.getAllCategories();

        // Assert
        assertEquals(2, categoryResponses.size());
        assertEquals(1L, categoryResponses.get(0).getCategoryId());
        assertEquals("Electronics", categoryResponses.get(0).getCategoryName());
        assertEquals(2L, categoryResponses.get(1).getCategoryId());
        assertEquals("Books", categoryResponses.get(1).getCategoryName());
    }


    @Test
    void testGetCategoryById() {
        // Arrange
        Category category = new Category(1L, "Electronics", null);
        when(categoryRepository.findById(1L)).thenReturn(java.util.Optional.of(category));

        // Act
        Category result = categoryService.getCategoryById(1L);

        // Assert
        assertEquals(1L, result.getCategoryId());
        assertEquals("Electronics", result.getCategoryName());
    }

    @Test
    void testCreateCategory() {
        // Arrange
        Category category = new Category(1L, "Electronics", null);

        // Act
        categoryService.createCategory(category);

        // Assert
        verify(categoryRepository).save(category);
    }
}

