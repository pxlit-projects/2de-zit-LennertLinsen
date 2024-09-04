package be.pxl.services.service;

import be.pxl.services.controller.ProductReviewDTO;
import be.pxl.services.domain.ProductReview;
import be.pxl.services.repository.ProductReviewRepository;
import be.pxl.services.service.ProductReviewService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Testcontainers
@SpringBootTest
@Transactional
public class ProductReviewServiceTest {

    @Container
    private static final MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:5.7.37");

    @Autowired
    private ProductReviewService productReviewService;

    @Autowired
    private ProductReviewRepository productReviewRepository;

    @DynamicPropertySource
    static void registerSQLProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create"); // Ensure schema is created
    }

    @Test
    void testAddProductReview() {
        ProductReviewDTO dto = new ProductReviewDTO("Great Product", "Really liked it", 5, "SupplierA");
        Long productId = 1L;

        ProductReview savedReview = productReviewService.addProductReview(dto, productId);

        assertNotNull(savedReview.getId());
        assertEquals(dto.getTitle(), savedReview.getTitle());
        assertEquals(dto.getDescription(), savedReview.getDescription());
        assertEquals(dto.getStars(), savedReview.getStars());
        assertEquals(dto.getSupplier(), savedReview.getSupplier());
        assertEquals(productId, savedReview.getProductId());
        assertNotNull(savedReview.getReviewTime());
    }

    @Test
    void testGetProductReviewsByProductId() {
        Long productId = 1L;
        ProductReview review1 = new ProductReview("Title1", "Description1", 4, "Supplier1", productId);
        ProductReview review2 = new ProductReview("Title2", "Description2", 5, "Supplier2", productId);

        productReviewRepository.save(review1);
        productReviewRepository.save(review2);

        List<ProductReview> reviews = productReviewService.getProductReviewsByProductId(productId);

        assertEquals(2, reviews.size());
        assertTrue(reviews.stream().anyMatch(r -> r.getTitle().equals("Title1")));
        assertTrue(reviews.stream().anyMatch(r -> r.getTitle().equals("Title2")));
    }

    @Test
    void testGetProductReviewsBySupplier() {
        String supplier = "SupplierX";
        ProductReview review1 = new ProductReview("Title1", "Description1", 4, supplier, 1L);
        ProductReview review2 = new ProductReview("Title2", "Description2", 5, supplier, 2L);

        productReviewRepository.save(review1);
        productReviewRepository.save(review2);

        List<ProductReview> reviews = productReviewService.getProductReviewsBySupplier(supplier);

        assertEquals(2, reviews.size());
        assertTrue(reviews.stream().anyMatch(r -> r.getTitle().equals("Title1")));
        assertTrue(reviews.stream().anyMatch(r -> r.getTitle().equals("Title2")));
    }
}
