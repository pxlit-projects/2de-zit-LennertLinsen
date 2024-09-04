package be.pxl.services.controller;

import be.pxl.services.domain.Category;
import be.pxl.services.domain.Product;
import be.pxl.services.service.ICategoryService;
import be.pxl.services.service.IProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringJUnitConfig
public class ProductControllerTest {

    @Mock
    private IProductService productService;

    @Mock
    private ICategoryService categoryService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddProduct() {
        ProductDTO newProductDTO = ProductDTO.builder()
                .productName("Product 1")
                .description("Description")
                .categoryId(1L)
                .stock(10)
                .price(100.0)
                .supplier("Supplier")
                .build();

        Product addedProduct = new Product(); // Create a Product instance accordingly
        when(productService.addProduct(newProductDTO)).thenReturn(addedProduct);

        ResponseEntity<ProductDTO> response = productController.addProduct(newProductDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(newProductDTO, response.getBody());
    }

    @Test
    public void testAddedToWishlist() {
        Long productId = 1L;

        // Create a Product object with the necessary properties
        Product product = Product.builder()
                .id(productId)
                .productName("Test Product")
                .description("Test Description")
                .category(new Category()) // You may need to set a category
                .stock(10)
                .price(99.99)
                .supplier("Test Supplier")
                .wishlisted(5)
                .build();

        // Configure the mock to return the above product
        when(productService.addToWishListCounter(productId)).thenReturn(product);

        // Call the controller method
        ResponseEntity<Product> response = productController.addedToWishlist(productId);

        // Check that the response status is 200 OK
        assertEquals(200, response.getStatusCodeValue());

        // Check that the body of the response matches the expected product
        assertEquals(product, response.getBody());
    }


    @Test
    public void testRemoveFromStock() {
        Long productId = 1L;
        Product product = new Product(); // Create a Product instance accordingly
        when(productService.removeFromStock(productId)).thenReturn(product);

        ResponseEntity<Product> response = productController.removeFromStock(productId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(product, response.getBody());
    }

    @Test
    public void testGetAllProducts() {
        List<ProductResponse> products = Arrays.asList(
                ProductResponse.builder().id(1L).productName("Product 1").build(),
                ProductResponse.builder().id(2L).productName("Product 2").build()
        );
        when(productService.getAllProducts()).thenReturn(products);

        ResponseEntity<List<ProductResponse>> response = ResponseEntity.ok(products);
        List<ProductResponse> responseBody = response.getBody();

        assertEquals(products, responseBody);
    }

    @Test
    public void testUpdateProduct() {
        Long productId = 1L;
        ProductUpdateDTO dto = ProductUpdateDTO.builder().productName("Updated Product").build();
        ProductDTO updatedProductDTO = ProductDTO.builder().productName("Updated Product").build();
        when(productService.updateProduct(productId, dto)).thenReturn(updatedProductDTO);

        ResponseEntity<ProductDTO> response = productController.updateProduct(productId, dto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(updatedProductDTO, response.getBody());
    }

    @Test
    public void testRefillStock() {
        Long productId = 1L;
        int quantity = 20;
        ProductDTO productDTO = ProductDTO.builder().stock(20).build();
        when(productService.refillStock(productId, quantity)).thenReturn(productDTO);

        ResponseEntity<ProductDTO> response = productController.refillStock(productId, quantity);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(productDTO, response.getBody());
    }

    @Test
    public void testGetProductsByCategory() {
        Long categoryId = 1L;
        Category category = new Category(); // Create a Category instance accordingly
        List<ProductResponse> productResponses = Collections.singletonList(
                ProductResponse.builder().id(1L).build()
        );
        when(categoryService.getCategoryById(categoryId)).thenReturn(category);
        when(productService.getProductsByCategory(category)).thenReturn(productResponses);

        ResponseEntity<List<ProductResponse>> response = productController.getProductsByCategory(categoryId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(productResponses, response.getBody());
    }

    @Test
    public void testDeleteProduct() {
        Long productId = 1L;
        when(productService.deleteProduct(productId)).thenReturn(true);

        ResponseEntity<Void> response = productController.deleteProduct(productId);

        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    public void testFindProductById() {
        Long productId = 1L;

        // Create the expected ProductResponse object
        ProductResponse expectedResponse = ProductResponse.builder()
                .id(productId)
                .productName("Test Product")
                .description("Test Description")
                .stock(10)
                .price(99.99)
                .categoryId(1L)
                .categoryName("Test Category")
                .supplier("Test Supplier")
                .wishlisted(5)
                .build();

        // Mock the service call to return the expected ProductResponse
        when(productService.getProductById(productId)).thenReturn(expectedResponse);

        // Call the controller method
        ResponseEntity<ProductResponse> response = productController.findProductById(productId);

        // Check that the response status is 200 OK
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Check that the body of the response matches the expected ProductResponse
        assertEquals(expectedResponse, response.getBody());
    }

}
