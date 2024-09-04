package be.pxl.services.controller;

import be.pxl.services.domain.Cart;
import be.pxl.services.domain.CartItem;
import be.pxl.services.service.ICartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CartControllerTest {

    @InjectMocks
    private CartController cartController;

    @Mock
    private ICartService cartService;

    @Mock
    private RabbitTemplate rabbitTemplate;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetCartById() throws Exception {
        Cart cart = new Cart(1L);
        when(cartService.getCartById(1L)).thenReturn(cart);

        mockMvc.perform(get("/api/shoppingcart/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(cart)));
    }

    @Test
    public void testCreateCart() throws Exception {
        Cart newCart = new Cart(1L);
        doNothing().when(cartService).addCart(any(Cart.class)); // Mocking void method

        mockMvc.perform(post("/api/shoppingcart/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(newCart)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testAddItemToCart() throws Exception {
        Cart cart = new Cart(1L);
        when(cartService.addItemToCart(1L, 1L)).thenReturn(cart);

        mockMvc.perform(put("/api/shoppingcart/addItemToCart/1/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(cart)));
    }

    @Test
    public void testRemoveItemFromCart() throws Exception {
        Cart cart = new Cart(1L);
        when(cartService.removeItemFromCart(1L, 1L)).thenReturn(cart);

        mockMvc.perform(put("/api/shoppingcart/removeItemFromCart/1/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(cart)));

        verify(rabbitTemplate).convertAndSend("products-queue", 1L);
    }
}
