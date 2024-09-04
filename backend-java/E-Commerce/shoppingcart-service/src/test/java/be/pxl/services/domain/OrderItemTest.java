package be.pxl.services.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OrderItemTest {

    @Test
    void testOrderItemCreation() {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(1L);
        orderItem.setCartId(2L);
        orderItem.setProductId(3L);
        orderItem.setAmount(5);
        orderItem.setOrderId(4L);

        assertNotNull(orderItem);
        assertEquals(1L, orderItem.getId());
        assertEquals(2L, orderItem.getCartId());
        assertEquals(3L, orderItem.getProductId());
        assertEquals(5, orderItem.getAmount());
        assertEquals(4L, orderItem.getOrderId());
    }

    @Test
    void testOrderItemWithBuilder() {
        OrderItem orderItem = OrderItem.builder()
                .id(1L)
                .cartId(2L)
                .productId(3L)
                .amount(5)
                .orderId(4L)
                .build();

        assertEquals(1L, orderItem.getId());
        assertEquals(2L, orderItem.getCartId());
        assertEquals(3L, orderItem.getProductId());
        assertEquals(5, orderItem.getAmount());
        assertEquals(4L, orderItem.getOrderId());
    }

    @Test
    void testOrderItemConstructor() {
        OrderItem orderItem = new OrderItem(2L, 3L, 5);

        assertNotNull(orderItem);
        assertNull(orderItem.getId()); // ID should be null because it's not set in the constructor
        assertEquals(2L, orderItem.getCartId());
        assertEquals(3L, orderItem.getProductId());
        assertEquals(5, orderItem.getAmount());
        assertNull(orderItem.getOrderId()); // Order ID should be null because it's not set in the constructor
    }
}
