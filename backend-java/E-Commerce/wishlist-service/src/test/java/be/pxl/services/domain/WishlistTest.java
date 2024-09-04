package be.pxl.services.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WishlistTest {

    @Test
    void testDefaultConstructor() {
        // Given
        Wishlist wishlist = new Wishlist();

        // When
        List<Long> productIds = wishlist.getProductIds();
        Long userId = wishlist.getUserId();

        // Then
        assertNotNull(productIds, "Product IDs list should be initialized");
        assertTrue(productIds.isEmpty(), "Product IDs list should be initially empty");
        assertNull(userId, "User ID should be initially null");
    }

    @Test
    void testParameterizedConstructor() {
        // Given
        Long userId = 123L;
        Wishlist wishlist = new Wishlist();
        wishlist.setUserId(userId);

        // When
        List<Long> productIds = wishlist.getProductIds();
        Long actualUserId = wishlist.getUserId();

        // Then
        assertNotNull(productIds, "Product IDs list should be initialized");
        assertTrue(productIds.isEmpty(), "Product IDs list should be initially empty");
        assertEquals(userId, actualUserId, "User ID should be correctly initialized");
    }

    @Test
    void testAddProduct() {
        // Given
        Wishlist wishlist = new Wishlist();
        Long productId = 1L;

        // When
        wishlist.addProduct(productId);

        // Then
        assertTrue(wishlist.getProductIds().contains(productId), "Product ID should be added to the list");
    }

    @Test
    void testRemoveProduct() {
        // Given
        Wishlist wishlist = new Wishlist();
        Long productId = 1L;
        wishlist.addProduct(productId);

        // When
        wishlist.removeItem(productId);

        // Then
        assertFalse(wishlist.getProductIds().contains(productId), "Product ID should be removed from the list");
    }

    @Test
    void testRemoveNonExistentProduct() {
        // Given
        Wishlist wishlist = new Wishlist();
        Long productId = 1L;

        // When
        wishlist.removeItem(productId);

        // Then
        // No exception should be thrown and no product ID should be in the list
        assertTrue(wishlist.getProductIds().isEmpty(), "Product IDs list should be empty");
    }

    @Test
    void testAddAndRemoveProduct() {
        // Given
        Wishlist wishlist = new Wishlist();
        Long productId = 1L;
        wishlist.addProduct(productId);

        // When
        wishlist.removeItem(productId);

        // Then
        assertTrue(wishlist.getProductIds().isEmpty(), "Product IDs list should be empty after removal");
    }

    @Test
    void testAddAndRemoveMultipleProducts() {
        // Given
        Wishlist wishlist = new Wishlist();
        Long productId1 = 1L;
        Long productId2 = 2L;
        wishlist.addProduct(productId1);
        wishlist.addProduct(productId2);

        // When
        wishlist.removeItem(productId1);
        wishlist.removeItem(productId2);

        // Then
        assertTrue(wishlist.getProductIds().isEmpty(), "Product IDs list should be empty after removing all products");
    }

    @Test
    void testRemoveNullProduct() {
        // Given
        Wishlist wishlist = new Wishlist();
        wishlist.addProduct(1L);

        // When
        wishlist.removeItem(null);

        // Then
        // No exception should be thrown and list should contain the existing product
        assertTrue(wishlist.getProductIds().contains(1L), "Product ID should still be in the list");
    }
}
