package be.pxl.services.service;

import be.pxl.services.controller.CategoryResponse;
import be.pxl.services.domain.Category;

import java.util.List;

public interface ICategoryService {
    List<CategoryResponse> getAllCategories();

    Category getCategoryById(Long categoryId);

    void createCategory(Category newCategory);
}
