package be.pxl.services.repository;

import be.pxl.services.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findCategoryByCategoryId(Long categoryId);
}
