package be.pxl.services.service;

import be.pxl.services.client.CatalogueClient;
import be.pxl.services.domain.Cart;
import be.pxl.services.domain.CartItem;
import be.pxl.services.repository.CartItemRepository;
import be.pxl.services.repository.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartServiceTest {

    @InjectMocks
    private CartService cartService;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private CatalogueClient catalogueClient;

    private Cart cart;
    private CartItem cartItem;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        cart = Cart.builder()
                .id(1L)
                .cartItems(new ArrayList<>()) // Ensure list is initialized
                .build();

        cartItem = CartItem.builder()
                .id(1L)
                .cartId(1L)
                .productId(1L)
                .amount(1)
                .build();
    }

    @Test
    void testGetCartById() {
        when(cartRepository.getCartById(1L)).thenReturn(cart);

        Cart result = cartService.getCartById(1L);

        assertNotNull(result);
        assertEquals(cart.getId(), result.getId());
    }

    @Test
    void testAddCart() {
        cartService.addCart(cart);

        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void testAddItemToCart_ExistingItem() {
        when(cartRepository.getCartById(1L)).thenReturn(cart);
        when(cartItemRepository.getCartItemByCartIdAndProductId(1L, 1L)).thenReturn(cartItem);

        cartService.addItemToCart(1L, 1L);

        verify(cartItemRepository, times(1)).save(cartItem);
        verify(cartRepository, times(1)).save(cart);
        verify(catalogueClient, times(1)).addToCart(1L);
    }

    @Test
    void testRemoveItemFromCart_ItemExists() {
        when(cartRepository.getCartById(1L)).thenReturn(cart);
        when(cartItemRepository.getCartItemByCartIdAndProductId(1L, 1L)).thenReturn(cartItem);

        cartService.removeItemFromCart(1L, 1L);

        verify(cartItemRepository, times(1)).save(cartItem);
        verify(cartRepository, times(1)).save(cart);
        verify(rabbitTemplate, times(1)).convertAndSend("products-queue", 1L);
    }

    @Test
    void testRemoveItemFromCart_ItemDoesNotExist() {
        when(cartRepository.getCartById(1L)).thenReturn(cart);
        when(cartItemRepository.getCartItemByCartIdAndProductId(1L, 1L)).thenReturn(null);

        Cart result = cartService.removeItemFromCart(1L, 1L);

        assertNull(result);
    }

    @Test
    void testRemoveItemFromCart_EmptyCartItem() {
        cartItem.setAmount(0);
        when(cartRepository.getCartById(1L)).thenReturn(cart);
        when(cartItemRepository.getCartItemByCartIdAndProductId(1L, 1L)).thenReturn(cartItem);

        Cart result = cartService.removeItemFromCart(1L, 1L);

        assertNull(result);
        verify(cartRepository, times(0)).save(cart);
    }
}
