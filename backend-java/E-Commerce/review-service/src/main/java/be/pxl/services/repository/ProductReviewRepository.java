package be.pxl.services.repository;

import be.pxl.services.domain.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {
    List<ProductReview> findProductReviewsByProductId(Long productId);

    List<ProductReview> findProductReviewsBySupplier(String supplier);
}
