package be.pxl.services.service;

import be.pxl.services.domain.Order;

import java.util.List;

public interface IOrderService {
    List<Order> getAll(Long userId);

    Order createOrder(Long userId, double totalPrice);
}
