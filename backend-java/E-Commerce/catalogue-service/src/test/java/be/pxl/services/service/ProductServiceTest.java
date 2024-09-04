package be.pxl.services.service;

import be.pxl.services.controller.ProductDTO;
import be.pxl.services.controller.ProductResponse;
import be.pxl.services.controller.ProductUpdateDTO;
import be.pxl.services.domain.Category;
import be.pxl.services.domain.Product;
import be.pxl.services.repository.CategoryRepository;
import be.pxl.services.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class ProductServiceTest {
    @Container
    private static final MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:5.7.37");

    static {
        mysqlContainer.start();
    }

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DynamicPropertySource
    static void registerSQLProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
    }

    @Test
    void testAddProduct() {
        // Arrange
        ProductDTO productDTO = new ProductDTO("Product1", "Description1", 1L, 10, 100.0, "Supplier1");
        Category category = new Category(1L, "Category1", new ArrayList<>());
        Product product = new Product("Product1", "Description1", 10, 100.0, "Supplier1");

        when(categoryRepository.findCategoryByCategoryId(1L)).thenReturn(category);
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArguments()[0]);

        // Act
        Product result = productService.addProduct(productDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Product1", result.getProductName());
        verify(categoryRepository).findCategoryByCategoryId(1L);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void testGetAllProducts() {

        Category category = new Category(1L, "Category1", new ArrayList<>());
        categoryRepository.save(category);

        // Arrange
        Product product = new Product(1L, "Product1", "Description1", category, 10, 100.0, "Supplier1", 0);
        List<Product> productList = List.of(product);

        when(productRepository.findAll()).thenReturn(productList);

        // Act
        List<ProductResponse> result = productService.getAllProducts();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Product1", result.get(0).getProductName());
    }

    @Test
    void testUpdateProduct() {
        // Arrange
        Product existingProduct = new Product(1L, "Product1", "Description1", new Category(1L, "Category1", new ArrayList<>()), 10, 100.0, "Supplier1", 0);
        ProductUpdateDTO updateDTO = new ProductUpdateDTO("UpdatedProduct", "UpdatedDescription", 150.0, 1L, 20, "UpdatedSupplier");
        Category newCategory = new Category(1L, "UpdatedCategory", new ArrayList<>());

        when(productRepository.findProductById(1L)).thenReturn(existingProduct);
        when(categoryRepository.findCategoryByCategoryId(1L)).thenReturn(newCategory);
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArguments()[0]);

        // Act
        ProductDTO result = productService.updateProduct(1L, updateDTO);

        // Assert
        assertNotNull(result);
        assertEquals("UpdatedProduct", result.getProductName());
        assertEquals("UpdatedDescription", result.getDescription());
        assertEquals(150.0, result.getPrice());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void testRefillStock() {
        // Arrange
        Product existingProduct = new Product(1L, "Product1", "Description1", new Category(1L, "Category1", new ArrayList<>()), 10, 100.0, "Supplier1", 0);
        when(productRepository.findProductById(1L)).thenReturn(existingProduct);
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArguments()[0]);

        // Act
        ProductDTO result = productService.refillStock(1L, 5);

        // Assert
        assertNotNull(result);
        assertEquals(15, result.getStock());
    }

    @Test
    void testDeleteProduct() {
        // Arrange
        Product product = new Product(1L, "Product1", "Description1", new Category(1L, "Category1", new ArrayList<>()), 10, 100.0, "Supplier1", 0);
        when(productRepository.existsById(1L)).thenReturn(true);
        when(productRepository.findProductById(1L)).thenReturn(product);
        when(categoryRepository.findCategoryByCategoryId(1L)).thenReturn(product.getCategory());
        doNothing().when(productRepository).deleteById(1L);

        // Act
        boolean result = productService.deleteProduct(1L);

        // Assert
        assertTrue(result);
        verify(productRepository).deleteById(1L);
    }

    @Test
    void testAddToWishListCounter() {
        // Arrange
        Product product = new Product(1L, "Product1", "Description1", new Category(1L, "Category1", new ArrayList<>()), 10, 100.0, "Supplier1", 0);
        when(productRepository.findProductById(1L)).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArguments()[0]);

        // Act
        Product result = productService.addToWishListCounter(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getWishlisted());
    }

    @Test
    void testRemoveFromStock() {
        // Arrange
        Product product = new Product(1L, "Product1", "Description1", new Category(1L, "Category1", new ArrayList<>()), 10, 100.0, "Supplier1", 0);
        when(productRepository.findProductById(1L)).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArguments()[0]);

        // Act
        Product result = productService.removeFromStock(1L);

        // Assert
        assertNotNull(result);
        assertEquals(9, result.getStock());
    }
}
