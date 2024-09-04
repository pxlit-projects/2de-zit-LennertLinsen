package be.pxl.services.service;

import be.pxl.services.controller.ProductDTO;
import be.pxl.services.controller.ProductResponse;
import be.pxl.services.controller.ProductUpdateDTO;
import be.pxl.services.domain.Category;
import be.pxl.services.domain.Product;
import be.pxl.services.repository.CategoryRepository;
import be.pxl.services.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    public Product addProduct(ProductDTO productData) {
        logger.info("adding a product.");
        Product newProduct = new Product(productData.getProductName(), productData.getDescription(), productData.getStock(), productData.getPrice(), productData.getSupplier());

        Category category = categoryRepository.findCategoryByCategoryId(productData.getCategoryId());

        newProduct.setCategory(category);

        category.getProducts().add(newProduct);

        productRepository.save(newProduct);
        logger.debug("added a product to the catalogue " + newProduct.getId());
        return newProduct;
    }

    public List<ProductResponse> getAllProducts() {

        logger.info("retrieving all products.");

        return productRepository.findAll().stream()
                .map(p -> new ProductResponse(
                        p.getId(),
                        p.getProductName(),
                        p.getDescription(),
                        p.getStock(),
                        p.getPrice(),
                        p.getCategory().getCategoryId(),
                        p.getCategory().getCategoryName(),
                        p.getSupplier(),
                        p.getWishlisted())).toList();
    }

    public ProductDTO updateProduct(Long productId, ProductUpdateDTO dto) {
        Product existingProduct = productRepository.findProductById(productId);

        existingProduct.setProductName(dto.getProductName());
        existingProduct.setDescription(dto.getDescription());
        existingProduct.setPrice(dto.getPrice());
        Category cat = categoryRepository.findCategoryByCategoryId(dto.getCategoryId());
        existingProduct.setCategory(cat);
        existingProduct.setStock(dto.getStock());
        existingProduct.setSupplier(dto.getSupplier());

        productRepository.save(existingProduct);

        ProductDTO product = new ProductDTO(existingProduct.getProductName(), existingProduct.getDescription(), existingProduct.getCategory().getCategoryId(), existingProduct.getStock(), existingProduct.getPrice(), existingProduct.getSupplier());
        logger.info("Updated product with id: " + productId);
        return product;
    }


    public ProductDTO refillStock(Long productId, int quantity) {
        Product existingProduct = productRepository.findProductById(productId);

        existingProduct.setStock(existingProduct.getStock() + quantity);

        productRepository.save(existingProduct);
        logger.info("Refilled stock of item with id " + productId + " by adding quantity: " + quantity);
        return new ProductDTO(existingProduct.getProductName(), existingProduct.getDescription(), existingProduct.getCategory().getCategoryId(), existingProduct.getStock(), existingProduct.getPrice(), existingProduct.getSupplier());
    }

    public List<ProductResponse> getProductsByCategory(Category category) {
        return productRepository.findByCategory(category).stream()
                .map(p -> new ProductResponse(
                        p.getId(),
                        p.getProductName(),
                        p.getDescription(),
                        p.getStock(),
                        p.getPrice(),
                        p.getCategory().getCategoryId(),
                        p.getCategory().getCategoryName(),
                        p.getSupplier(),
                        p.getWishlisted())).toList();
    }


    public boolean deleteProduct(Long productId) {
        if (productRepository.existsById(productId)) {
            Product product = productRepository.findProductById(productId);
            Category category = categoryRepository.findCategoryByCategoryId(product.getCategory().getCategoryId());
            category.getProducts().remove(product);
            categoryRepository.save(category);

            productRepository.deleteById(productId);
            logger.info("Product deleted from catalogue.");
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Product addToWishListCounter(Long productId) {
        Product product = productRepository.findProductById(productId);

        product.addToCounter();

        productRepository.save(product);
        logger.info("Wishlist counter for the item with id " + productId + "has gone up by 1.");
        return product;
    }

    @Override
    public ProductResponse getProductById(Long productId) {
        Product product = productRepository.findProductById(productId);
        logger.info("Retireving info about item with id: " + productId);
        return new ProductResponse(product.getId(), product.getProductName(), product.getDescription(), product.getStock(), product.getPrice(), product.getCategory().getCategoryId(), product.getCategory().getCategoryName(), product.getSupplier(), product.getWishlisted());
    }

    @Override
    public Product removeFromStock(Long productId) {
        Product product = productRepository.findProductById(productId);

        product.setStock(product.getStock() - 1);

        productRepository.save(product);
        logger.info("An item was added to a cart, so the stock has been reduced by one.");
        return product;
    }
}
