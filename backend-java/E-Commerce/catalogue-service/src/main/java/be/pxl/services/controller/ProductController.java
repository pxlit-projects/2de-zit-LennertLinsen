package be.pxl.services.controller;

import be.pxl.services.domain.Category;
import be.pxl.services.domain.Product;
import be.pxl.services.service.ICategoryService;
import be.pxl.services.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final IProductService productService;
    private final ICategoryService categoryService;

    @PostMapping("/add")
    public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO newProduct) {
        Product addedProduct = productService.addProduct(newProduct);
        return ResponseEntity.ok(newProduct);
    }

    @PostMapping("/wishlisted/{productId}")
    public ResponseEntity<Product> addedToWishlist(@PathVariable Long productId){
        Product product = productService.addToWishListCounter(productId);

        return ResponseEntity.ok(product);
    }

    @PostMapping("/removeFromStock/{productId}")
    public ResponseEntity<Product> removeFromStock(@PathVariable Long productId){
        Product product = productService.removeFromStock(productId);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/")
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long productId, @RequestBody ProductUpdateDTO dto) {
        ProductDTO product = productService.updateProduct(productId, dto);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //Api call maken moet requestbody gwn een getal zijn, geen json van maken.
    @PostMapping("/refill/{productId}")
    public ResponseEntity<ProductDTO> refillStock(@PathVariable Long productId, @RequestBody int quantity) {
        ProductDTO product = productService.refillStock(productId, quantity);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/byCategory/{categoryId}")
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(@PathVariable Long categoryId) {
        Category category = categoryService.getCategoryById(categoryId);

        if (category != null) {
            List<ProductResponse> productList = productService.getProductsByCategory(category);
            return ResponseEntity.ok(productList);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        boolean deleted = productService.deleteProduct(productId);

        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> findProductById(@PathVariable Long productId){
        return ResponseEntity.ok(productService.getProductById(productId));
    }
}
