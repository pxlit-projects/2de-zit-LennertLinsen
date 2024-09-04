package be.pxl.services.repository;

import be.pxl.services.domain.Category;
import be.pxl.services.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(Category category);
    Product findProductById(Long id);
}
