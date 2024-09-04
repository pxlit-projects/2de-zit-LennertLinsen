package be.pxl.services.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class CartTest {

    @Test
    void testCartCreation() {
        Cart cart = new Cart(1L);
        assertNotNull(cart);
        assertEquals(1L, cart.getId());
        assertNotNull(cart.getCartItems());
        assertTrue(cart.getCartItems().isEmpty());
    }

    @Test
    void testAddCartItem() {
        Cart cart = new Cart(1L);
        CartItem item = new CartItem();
        cart.getCartItems().add(item);
        assertEquals(1, cart.getCartItems().size());
        assertTrue(cart.getCartItems().contains(item));
    }

    @Test
    void testCartItemListInitialization() {
        Cart cart = new Cart(1L);
        List<CartItem> items = cart.getCartItems();
        assertNotNull(items);
        assertTrue(items instanceof ArrayList);
    }

    @Test
    void testCartWithBuilder() {
        Cart cart = Cart.builder()
                .id(1L)
                .cartItems(new ArrayList<>())
                .build();
        assertEquals(1L, cart.getId());
        assertNotNull(cart.getCartItems());
        assertTrue(cart.getCartItems().isEmpty());
    }
}
