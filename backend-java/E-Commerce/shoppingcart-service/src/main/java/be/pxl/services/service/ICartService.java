package be.pxl.services.service;

import be.pxl.services.domain.Cart;
import be.pxl.services.repository.CartRepository;
import org.springframework.web.bind.annotation.GetMapping;

public interface ICartService {

    Cart getCartById(Long cartId);

    void addCart(Cart newCart);

    Cart addItemToCart(Long cartId, Long productId);

    Cart removeItemFromCart(Long cartId, Long productId);
}
