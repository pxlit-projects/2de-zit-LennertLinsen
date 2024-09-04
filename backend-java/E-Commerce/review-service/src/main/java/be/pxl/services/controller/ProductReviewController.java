package be.pxl.services.controller;

import be.pxl.services.domain.ProductReview;
import be.pxl.services.service.IProductReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productreviews")
@RequiredArgsConstructor
public class ProductReviewController {

    private final IProductReviewService _productReviewService;

    @PostMapping("/add/{productId}")
    public ResponseEntity<ProductReview> addProductReview(@PathVariable Long productId, @RequestBody ProductReviewDTO dto) {
        ProductReview addedReview = _productReviewService.addProductReview(dto, productId);
        return ResponseEntity.ok(addedReview);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<List<ProductReview>> getProductReviewsByProductId(@PathVariable Long productId) {

        if (productId != null){
            return ResponseEntity.ok(_productReviewService.getProductReviewsByProductId(productId));
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/bySupplier")
    public ResponseEntity<List<ProductReview>> getProductReviewsBySupplier(@RequestParam("supplier") String supplier) {

        if (supplier != null){
            return ResponseEntity.ok(_productReviewService.getProductReviewsBySupplier(supplier));
        }else{
            return ResponseEntity.notFound().build();
        }
    }
}
