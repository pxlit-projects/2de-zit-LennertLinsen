package be.pxl.services.repository;

import be.pxl.services.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart getCartById(Long id);
}
