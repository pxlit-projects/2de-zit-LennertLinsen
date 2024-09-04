package be.pxl.services.services;

import be.pxl.services.domain.Wishlist;

public interface IWishlistService {
    void addWishlist(Wishlist newWishlist);

    Wishlist addItemToWishlist(Long wishlistId, Long productId);

    Wishlist removeItemFromWishlist(Long wishlistId, Long productId);

    Wishlist getWishlistByWishlistId(Long wishlistId);

    Wishlist getWishlistByUserId(Long userId);
}
