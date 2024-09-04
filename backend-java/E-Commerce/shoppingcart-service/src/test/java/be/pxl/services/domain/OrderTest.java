package be.pxl.services.domain;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {

    @Test
    void testOrderCreation() {
        Order order = new Order();
        order.setOrderId(1L);
        order.setUserId(2L);
        order.setTotalPrice(100.50);
        order.setOrderItems(Collections.emptyList());

        assertNotNull(order);
        assertEquals(1L, order.getOrderId());
        assertEquals(2L, order.getUserId());
        assertEquals(100.50, order.getTotalPrice());
        assertTrue(order.getOrderItems().isEmpty());
    }

    @Test
    void testOrderWithBuilder() {
        Order order = Order.builder()
                .orderId(1L)
                .userId(2L)
                .totalPrice(100.50)
                .orderItems(Collections.singletonList(new OrderItem()))
                .build();

        assertEquals(1L, order.getOrderId());
        assertEquals(2L, order.getUserId());
        assertEquals(100.50, order.getTotalPrice());
        assertEquals(1, order.getOrderItems().size());
    }

    @Test
    void testAddOrderItem() {
        OrderItem item = new OrderItem();
        item.setOrderId(1L);
        item.setProductId(2L);
        item.setAmount(5);

        Order order = new Order();
        order.setOrderItems(new ArrayList<>());
        order.getOrderItems().add(item);

        assertEquals(1, order.getOrderItems().size());
        assertEquals(item, order.getOrderItems().get(0));
    }
}
