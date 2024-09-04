package be.pxl.services.service;

import be.pxl.services.controller.ProductDTO;
import be.pxl.services.controller.ProductResponse;
import be.pxl.services.controller.ProductUpdateDTO;
import be.pxl.services.domain.Category;
import be.pxl.services.domain.Product;

import java.util.List;

public interface IProductService {
    Product addProduct(ProductDTO product);

    List<ProductResponse> getAllProducts();

    ProductDTO updateProduct(Long productId, ProductUpdateDTO updatedProduct);

    ProductDTO refillStock(Long productId, int quantity);

    List<ProductResponse> getProductsByCategory(Category category);

    boolean deleteProduct(Long productId);

    Product addToWishListCounter(Long productId);

    ProductResponse getProductById(Long productId);

    Product removeFromStock(Long productId);
}
