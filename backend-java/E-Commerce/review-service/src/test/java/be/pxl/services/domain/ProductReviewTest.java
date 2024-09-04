package be.pxl.services.domain;

import be.pxl.services.domain.ProductReview;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;

public class ProductReviewTest {

    @Test
    public void testDefaultConstructor() {
        ProductReview review = new ProductReview();
        assertNotNull(review);
        assertNull(review.getTitle());
        assertNull(review.getDescription());
        assertEquals(0, review.getStars());
        assertNull(review.getReviewTime());
        assertNull(review.getProductId());
        assertNull(review.getSupplier());
    }

    @Test
    public void testParameterizedConstructor() {
        String title = "Great Product";
        String description = "I really enjoyed using this product.";
        int stars = 5;
        String supplier = "SupplierX";
        Long productId = 123L;

        ProductReview review = new ProductReview(title, description, stars, supplier, productId);

        assertNotNull(review);
        assertEquals(title, review.getTitle());
        assertEquals(description, review.getDescription());
        assertEquals(stars, review.getStars());
        assertNotNull(review.getReviewTime()); // This will be checked against the current time
        assertEquals(productId, review.getProductId());
        assertEquals(supplier, review.getSupplier());
    }

    @Test
    public void testBuilder() {
        ProductReview review = ProductReview.builder()
                .title("Great Product")
                .description("I really enjoyed using this product.")
                .stars(5)
                .supplier("SupplierX")
                .productId(123L)
                .build();

        assertNotNull(review);
        assertEquals("Great Product", review.getTitle());
        assertEquals("I really enjoyed using this product.", review.getDescription());
        assertEquals(5, review.getStars());
        assertEquals(123L, review.getProductId());
        assertEquals("SupplierX", review.getSupplier());
    }

    @Test
    public void testSettersAndGetters() {
        ProductReview review = new ProductReview();
        review.setTitle("Excellent");
        review.setDescription("Best product ever!");
        review.setStars(4);
        review.setReviewTime(LocalDateTime.of(2024, 9, 3, 12, 0));
        review.setProductId(456L);
        review.setSupplier("SupplierY");

        assertEquals("Excellent", review.getTitle());
        assertEquals("Best product ever!", review.getDescription());
        assertEquals(4, review.getStars());
        assertEquals(LocalDateTime.of(2024, 9, 3, 12, 0), review.getReviewTime());
        assertEquals(456L, review.getProductId());
        assertEquals("SupplierY", review.getSupplier());
    }
}
