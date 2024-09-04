package be.pxl.services.service;

import be.pxl.services.clients.CatalogueClient;
import be.pxl.services.domain.Wishlist;
import be.pxl.services.repository.WishlistRepository;
import be.pxl.services.services.WishlistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Testcontainers
@SpringBootTest
public class WishlistServiceTest {

    @Container
    private static final MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:5.7.37");

    @Mock
    private WishlistRepository wishlistRepository;

    @Mock
    private CatalogueClient catalogueClient;

    @InjectMocks
    private WishlistService wishlistService;


    @DynamicPropertySource
    static void registerSQLProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
    }

    @Test
    void testAddWishlist() {
        // Given
        Wishlist newWishlist = new Wishlist();
        when(wishlistRepository.save(any(Wishlist.class))).thenReturn(newWishlist);

        // When
        wishlistService.addWishlist(newWishlist);

        // Then
        verify(wishlistRepository).save(newWishlist);
    }

    @Test
    void testAddItemToWishlist() {
        // Given
        Long userId = 1L;
        Long productId = 100L;
        Wishlist wishlist = new Wishlist();
        when(wishlistRepository.getWishlistByUserId(userId)).thenReturn(wishlist);
        when(wishlistRepository.save(any(Wishlist.class))).thenReturn(wishlist);

        // When
        Wishlist updatedWishlist = wishlistService.addItemToWishlist(userId, productId);

        // Then
        verify(wishlistRepository).getWishlistByUserId(userId);
        verify(wishlistRepository).save(wishlist);
        verify(catalogueClient).addedToWishlist(productId);
        assertTrue(updatedWishlist.getProductIds().contains(productId), "Product ID should be added to the wishlist");
    }

    @Test
    void testRemoveItemFromWishlist() {
        // Given
        Long userId = 1L;
        Long productId = 100L;
        Wishlist wishlist = new Wishlist();
        wishlist.addProduct(productId);
        when(wishlistRepository.getWishlistByUserId(userId)).thenReturn(wishlist);
        when(wishlistRepository.save(any(Wishlist.class))).thenReturn(wishlist);

        // When
        Wishlist updatedWishlist = wishlistService.removeItemFromWishlist(userId, productId);

        // Then
        verify(wishlistRepository).getWishlistByUserId(userId);
        verify(wishlistRepository).save(wishlist);
        assertFalse(updatedWishlist.getProductIds().contains(productId), "Product ID should be removed from the wishlist");
    }

    @Test
    void testGetWishlistByWishlistId() {
        // Given
        Long wishlistId = 1L;
        Wishlist wishlist = new Wishlist();
        when(wishlistRepository.getWishlistByWishlistId(wishlistId)).thenReturn(wishlist);

        // When
        Wishlist foundWishlist = wishlistService.getWishlistByWishlistId(wishlistId);

        // Then
        verify(wishlistRepository).getWishlistByWishlistId(wishlistId);
        assertNotNull(foundWishlist, "Wishlist should be found");
    }

    @Test
    void testGetWishlistByUserId() {
        // Given
        Long userId = 1L;
        Wishlist wishlist = new Wishlist();
        when(wishlistRepository.getWishlistByUserId(userId)).thenReturn(wishlist);

        // When
        Wishlist foundWishlist = wishlistService.getWishlistByUserId(userId);

        // Then
        verify(wishlistRepository).getWishlistByUserId(userId);
        assertNotNull(foundWishlist, "Wishlist should be found");
    }
}
