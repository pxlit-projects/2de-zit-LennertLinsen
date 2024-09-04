package be.pxl.services.repository;

import be.pxl.services.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> getAllByUserId(Long UserId);
}
