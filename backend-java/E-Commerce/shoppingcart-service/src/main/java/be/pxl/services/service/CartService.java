package be.pxl.services.service;

import be.pxl.services.client.CatalogueClient;
import be.pxl.services.domain.Cart;
import be.pxl.services.domain.CartItem;
import be.pxl.services.repository.CartItemRepository;
import be.pxl.services.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService{
    private final CartRepository _cartRepository;
    private final CartItemRepository _cartItemRepository;
    private final RabbitTemplate rabbitTemplate;
    private final CatalogueClient catalogueClient;
    private static final Logger logger = LoggerFactory.getLogger(CartService.class);


    @Override
    public Cart getCartById(Long cartId) {
        logger.info("Retrieving cart by id: " + cartId);
        return _cartRepository.getCartById(cartId);
    }

    @Override
    public void addCart(Cart newCart) {
        logger.info("adding new cart " + newCart.getId());
        _cartRepository.save(newCart);
    }

    @Override
    public Cart addItemToCart(Long cartId, Long productId) {
        logger.info("Adding item to cart " + cartId + " with productId: " + productId);
        CartItem cartItem = _cartItemRepository.getCartItemByCartIdAndProductId(cartId, productId);
        Cart cart = _cartRepository.getCartById(cartId);

        if (cartItem != null){
            logger.info("Item already exists in cart, so the count for that item will go up.");
            cartItem.setAmount(cartItem.getAmount() + 1);
            _cartItemRepository.save(cartItem);
            if (!cart.getCartItems().contains(cartItem)){
                cart.getCartItems().add(cartItem);
            }
            _cartRepository.save(cart);
            catalogueClient.addToCart(productId);
        }else{
            logger.info("Item doesn't exist in cart yet, so a new one is added.");
            CartItem newCartItem = new CartItem();
            newCartItem.setProductId(productId);
            newCartItem.setCartId(cartId);
            newCartItem.setAmount(1);
            _cartItemRepository.save(newCartItem);
            cart.getCartItems().add(newCartItem);
            _cartRepository.save(cart);
        }

        return cart;
    }

    @Override
    public Cart removeItemFromCart(Long cartId, Long productId) {
        logger.info("Removing item from cart " + cartId + " with productId: " + productId);
        CartItem cartItem = _cartItemRepository.getCartItemByCartIdAndProductId(cartId, productId);
        Cart cart = _cartRepository.getCartById(cartId);

        if (cartItem == null){
            return null;
        }else{
            if (cartItem.getAmount() != 0){
                cartItem.setAmount(cartItem.getAmount() - 1);
                _cartItemRepository.save(cartItem);
                if (cartItem.getAmount() == 0){
                    logger.info("There is only one of this item in the cart, so the item removed completely.");
                    cart.getCartItems().remove(cartItem);
                    _cartRepository.save(cart);
                }else{
                    logger.info("Multiple amount of this item in cart, so amount goes down by one.");
                    _cartRepository.save(cart);
                }
            }else{
                return null;
            }
            rabbitTemplate.convertAndSend("products-queue", cartItem.getProductId());
            return cart;
        }
    }
}
