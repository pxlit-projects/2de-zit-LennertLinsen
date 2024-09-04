package be.pxl.services.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CartItemTest {

    @Test
    void testCartItemCreation() {
        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setCartId(2L);
        cartItem.setProductId(3L);
        cartItem.setAmount(5);

        assertNotNull(cartItem);
        assertEquals(1L, cartItem.getId());
        assertEquals(2L, cartItem.getCartId());
        assertEquals(3L, cartItem.getProductId());
        assertEquals(5, cartItem.getAmount());
    }

    @Test
    void testCartItemWithBuilder() {
        CartItem cartItem = CartItem.builder()
                .id(1L)
                .cartId(2L)
                .productId(3L)
                .amount(5)
                .build();

        assertEquals(1L, cartItem.getId());
        assertEquals(2L, cartItem.getCartId());
        assertEquals(3L, cartItem.getProductId());
        assertEquals(5, cartItem.getAmount());
    }
}
