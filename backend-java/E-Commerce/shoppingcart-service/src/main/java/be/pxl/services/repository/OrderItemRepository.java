package be.pxl.services.repository;

import be.pxl.services.domain.Order;
import be.pxl.services.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
