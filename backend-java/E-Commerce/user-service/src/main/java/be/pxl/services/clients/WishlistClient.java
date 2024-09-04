package be.pxl.services.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "wishlist-service")
public interface WishlistClient {
    @PostMapping("/api/wishlist/{userId}")
    void createWishlist(@PathVariable Long userId);
}
