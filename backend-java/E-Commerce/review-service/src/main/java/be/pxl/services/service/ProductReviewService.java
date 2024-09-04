package be.pxl.services.service;

import be.pxl.services.controller.ProductReviewDTO;
import be.pxl.services.domain.ProductReview;
import be.pxl.services.repository.ProductReviewRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductReviewService implements IProductReviewService {
    private final ProductReviewRepository _productReviewRepository;
    private static final Logger logger = LoggerFactory.getLogger(ProductReviewService.class);

    public List<ProductReview> getProductReviewsByProductId(Long productId){
        logger.info("Retrieving all productreviews for the product with id: " + productId);
        return _productReviewRepository.findProductReviewsByProductId(productId);
    }

    public List<ProductReview> getProductReviewsBySupplier(String supplier){
        logger.info("Retrieving all productreviews for the supplier: " + supplier);
        return _productReviewRepository.findProductReviewsBySupplier(supplier);
    }

    public ProductReview addProductReview(ProductReviewDTO dto, Long productId) {
        ProductReview newReview = new ProductReview(dto.getTitle(), dto.getDescription(), dto.getStars(), dto.getSupplier(), productId);
        logger.info("Adding review to product with id: " + productId + ", review: " + dto.getTitle());
        return _productReviewRepository.save(newReview);
    }
}
