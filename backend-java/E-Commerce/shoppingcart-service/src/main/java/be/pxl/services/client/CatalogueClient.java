package be.pxl.services.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "catalogue-service")
public interface CatalogueClient {
    @PostMapping("/api/products/removeFromStock/{productId}")
    void addToCart(@PathVariable Long productId);
}
