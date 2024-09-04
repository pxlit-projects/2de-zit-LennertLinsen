package be.pxl.services.controller;

import be.pxl.services.domain.Cart;
import be.pxl.services.service.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shoppingcart")
@RequiredArgsConstructor
public class CartController {

    private final ICartService _cartService;
    private final RabbitTemplate _rabbitTemplate;

    @GetMapping("/{cartId}")
    public ResponseEntity<Cart> getCartById(@PathVariable Long cartId){
        Cart cart = _cartService.getCartById(cartId);
        if (cart != null){
            return ResponseEntity.ok(cart);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void createCart(@PathVariable Long userId){
        Cart newCart = new Cart(userId);

        _cartService.addCart(newCart);
    }

    @PutMapping("/addItemToCart/{cartId}/{productId}")
    public ResponseEntity<Cart> addItemToCart(@PathVariable Long cartId, @PathVariable Long productId){
        Cart cart = _cartService.addItemToCart(cartId, productId);

        if(cart != null){

            return ResponseEntity.ok(cart);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/removeItemFromCart/{cartId}/{productId}")
    public ResponseEntity<Cart> removeItemFromCart(@PathVariable Long cartId, @PathVariable Long productId){
        Cart cart = _cartService.removeItemFromCart(cartId, productId);

        if(cart != null){
            _rabbitTemplate.convertAndSend("products-queue", productId);
            return ResponseEntity.ok(cart);
        }else{
            return ResponseEntity.notFound().build();
        }
    }


}
