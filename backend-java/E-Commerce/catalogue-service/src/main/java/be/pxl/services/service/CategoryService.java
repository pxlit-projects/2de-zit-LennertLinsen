package be.pxl.services.service;

import be.pxl.services.controller.CategoryResponse;
import be.pxl.services.domain.Category;
import be.pxl.services.domain.Product;
import be.pxl.services.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService{

    private final CategoryRepository categoryRepository;
    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);
    public List<CategoryResponse> getAllCategories() {

        logger.info("Retrieving all categories.");

        return categoryRepository.findAll().stream()
                .map(this::convertToCategoryResponse)
                .collect(Collectors.toList());
    }

    private CategoryResponse convertToCategoryResponse(Category category) {
        List<Long> productIds = category.getProducts().stream()
                .map(Product::getId)  // Extract the product ID
                .collect(Collectors.toList());

        return new CategoryResponse(
                category.getCategoryId(),
                category.getCategoryName(),
                productIds
        );
    }

    public Category getCategoryById(Long categoryId) {
        logger.info("Retrieving category by the categoryId:" + categoryId);
        return categoryRepository.findById(categoryId).orElse(null);
    }

    @Override
    public void createCategory(Category newCategory) {
        logger.info("Creating new category: " + newCategory.getCategoryId());
        categoryRepository.save(newCategory);
    }
}
