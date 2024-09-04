package be.pxl.services.repository;

import be.pxl.services.domain.Cart;
import be.pxl.services.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem getCartItemByCartIdAndProductId(Long cartId, Long ProductId);
}
