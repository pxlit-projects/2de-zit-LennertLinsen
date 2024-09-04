package be.pxl.services.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "catalogue-service")
public interface CatalogueClient {
    @PostMapping("/api/products/wishlisted/{productId}")
    void addedToWishlist(@PathVariable Long productId);
}
