package be.pxl.services.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "shoppingcart-service")
public interface CartClient {
    @PostMapping("/api/shoppingcart/{userId}")
    void createCart(@PathVariable Long userId);
}
