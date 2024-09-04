package be.pxl.services.listeners;

import be.pxl.services.domain.Product;
import be.pxl.services.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductStockListener {

    private final ProductRepository productRepository;

    @RabbitListener(queues = "products-queue")
    @Retryable(maxAttempts = 1, backoff = @Backoff(delay = 1000))
    public void listen(Long productId){
        Product product = productRepository.findProductById(productId);

        product.setStock(product.getStock() + 1);

        productRepository.save(product);
    }
}
