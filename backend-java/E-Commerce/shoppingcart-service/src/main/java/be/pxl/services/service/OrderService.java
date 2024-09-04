package be.pxl.services.service;

import be.pxl.services.domain.Cart;
import be.pxl.services.domain.CartItem;
import be.pxl.services.domain.Order;
import be.pxl.services.domain.OrderItem;
import be.pxl.services.repository.CartItemRepository;
import be.pxl.services.repository.CartRepository;
import be.pxl.services.repository.OrderItemRepository;
import be.pxl.services.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{
    private final OrderRepository _orderRepository;
    private final CartRepository _cartRepository;
    private final OrderItemRepository _orderItemRepository;
    private final CartItemRepository _cartItemRepository;
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    public List<Order> getAll(Long userId){
        return _orderRepository.getAllByUserId(userId);
    }


    public Order createOrder(Long userId, double totalPrice){
        logger.info("A new order is being created and the cart is being emptied.");
        Order newOrder = new Order(userId);

        Cart currentCart = _cartRepository.getCartById(userId);
        List<OrderItem> items = new ArrayList<>();

        for (CartItem c : currentCart.getCartItems()){
            OrderItem newItem = new OrderItem(userId, c.getProductId(), c.getAmount());
            newItem.setOrderId(newOrder.getOrderId());
            Cart cart = _cartRepository.getCartById(c.getCartId());
            cart.setCartItems(new ArrayList<>());
            _cartItemRepository.delete(c);
            _cartRepository.save(cart);
            _orderItemRepository.save(newItem);
            items.add(newItem);
        }

        newOrder.setTotalPrice(totalPrice);

        _orderRepository.save(newOrder);

        return newOrder;

    }
}
