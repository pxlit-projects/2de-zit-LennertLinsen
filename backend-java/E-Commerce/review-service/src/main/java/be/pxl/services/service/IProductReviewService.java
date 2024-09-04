package be.pxl.services.service;

import be.pxl.services.controller.ProductReviewDTO;
import be.pxl.services.domain.ProductReview;

import java.util.List;

public interface IProductReviewService {
    ProductReview addProductReview(ProductReviewDTO productReview, Long productId);

    List<ProductReview> getProductReviewsByProductId(Long productId);

    List<ProductReview> getProductReviewsBySupplier(String supplier);
}
