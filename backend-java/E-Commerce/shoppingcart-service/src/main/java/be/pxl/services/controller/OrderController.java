package be.pxl.services.controller;

import be.pxl.services.domain.CartItem;
import be.pxl.services.domain.Order;
import be.pxl.services.service.IOrderService;
import be.pxl.services.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final IOrderService _orderService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<Order>> getAllOrders(@PathVariable Long userId){
        List<Order> orders = _orderService.getAll(userId);

        if(orders != null){
            return ResponseEntity.ok(orders);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/createOrder/{userId}")
    public ResponseEntity<Order> createOrder(@PathVariable Long userId, @RequestBody double totalPrice){
        Order order = _orderService.createOrder(userId, totalPrice);

        if(order != null){
            return ResponseEntity.ok(order);
        }else{
            return ResponseEntity.notFound().build();
        }
    }
}
