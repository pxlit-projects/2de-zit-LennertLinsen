package be.pxl.services.service;

import be.pxl.services.domain.Cart;
import be.pxl.services.domain.CartItem;
import be.pxl.services.domain.Order;
import be.pxl.services.domain.OrderItem;
import be.pxl.services.repository.CartItemRepository;
import be.pxl.services.repository.CartRepository;
import be.pxl.services.repository.OrderItemRepository;
import be.pxl.services.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Testcontainers
@SpringBootTest
class OrderServiceTest {

    @Container
    private static final MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:5.7.37");

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private OrderService orderService;

    @DynamicPropertySource
    static void registerSQLProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
    }

    @Test
    void testGetAllOrders() {
        // Arrange
        Order order = new Order();
        order.setOrderId(1L);
        order.setUserId(1L);
        order.setTotalPrice(100.0);
        order.setOrderItems(new ArrayList<>());

        List<Order> orders = List.of(order);
        when(orderRepository.getAllByUserId(1L)).thenReturn(orders);

        // Act
        List<Order> result = orderService.getAll(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(order, result.get(0));
    }


    @Test
    void testCreateOrder() {
        // Mocking cart and cart items
        CartItem cartItem = new CartItem(); // Initialize `cartItem` as needed
        Cart cart = new Cart();
        cart.setId(1L);
        cart.getCartItems().add(cartItem);

        Cart updatedCart = new Cart();
        updatedCart.setId(1L);
        updatedCart.setCartItems(new ArrayList<>()); // Updated cart after clearing items

        when(cartRepository.getCartById(1L)).thenReturn(cart);
        when(cartRepository.getCartById(cartItem.getCartId())).thenReturn(updatedCart); // Mock the second call

        // Creating a mock Order object
        Order order = new Order();
        order.setOrderId(1L); // Ensure this ID is set in your mock
        order.setTotalPrice(100.0);

        // Stubbing the methods
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order o = invocation.getArgument(0);
            o.setOrderId(1L); // Simulate database ID assignment
            return o;
        });

        // Calling the method under test
        Order newOrder = orderService.createOrder(1L, 100.0);

        // Verifications
        assertNotNull(newOrder);
        assertEquals(1L, newOrder.getOrderId()); // ID should be set by mock
        assertEquals(100.0, newOrder.getTotalPrice());

        // Verify interactions
        verify(cartRepository, times(1)).getCartById(1L);
        verify(cartRepository, times(1)).getCartById(cartItem.getCartId()); // Verify the second call
        verify(orderRepository, times(1)).save(newOrder);
    }

}
