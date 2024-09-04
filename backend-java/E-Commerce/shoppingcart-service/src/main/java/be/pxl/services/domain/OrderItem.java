package be.pxl.services.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="orderItems")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long cartId;
    private Long productId;
    private int amount;
    private Long orderId;

    public OrderItem(Long cartId, Long productId, int amount){
        this.cartId = cartId;
        this.productId = productId;
        this.amount = amount;
    }
}
