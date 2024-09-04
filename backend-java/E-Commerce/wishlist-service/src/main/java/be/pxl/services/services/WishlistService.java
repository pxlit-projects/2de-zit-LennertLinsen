package be.pxl.services.services;

import be.pxl.services.clients.CatalogueClient;
import be.pxl.services.domain.Wishlist;
import be.pxl.services.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WishlistService implements IWishlistService{

    private final WishlistRepository _wishlistRepository;
    private final CatalogueClient _catalogueClient;
    private static final Logger logger = LoggerFactory.getLogger(WishlistService.class);

    public void addWishlist(Wishlist newWishlist){
        logger.info("A new wishlist is made by creating a new user.");
        _wishlistRepository.save(newWishlist);
    }

    @Override
    public Wishlist addItemToWishlist(Long userId, Long productId) {
        logger.info("Item with id: " + productId + " is added to the wishlist of user with id: " + userId);
        Wishlist wishlist = _wishlistRepository.getWishlistByUserId(userId);

        wishlist.addProduct(productId);
        _wishlistRepository.save(wishlist);

        _catalogueClient.addedToWishlist(productId);

        return wishlist;
    }

    @Override
    public Wishlist removeItemFromWishlist(Long userId, Long productId) {
        logger.info("Item with id: " + productId + " is removed from the wishlist of user with id: " + userId);
        Wishlist wishlist = _wishlistRepository.getWishlistByUserId(userId);

        wishlist.removeItem(productId);
        _wishlistRepository.save(wishlist);
        return wishlist;
    }

    @Override
    public Wishlist getWishlistByWishlistId(Long wishlistId) {
        logger.info("Wishlist is being retrieved by wishlistId: " + wishlistId);
        return _wishlistRepository.getWishlistByWishlistId(wishlistId);
    }

    @Override
    public Wishlist getWishlistByUserId(Long userId) {
        logger.info("Wishlist is being retrieved by userId: " + userId);
        return _wishlistRepository.getWishlistByUserId(userId);
    }
}
