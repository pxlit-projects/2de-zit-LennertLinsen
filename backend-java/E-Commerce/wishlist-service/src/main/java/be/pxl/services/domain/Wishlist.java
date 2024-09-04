package be.pxl.services.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="wishlists")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Wishlist {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long wishlistId;
    @ElementCollection
    private List<Long> productIds = new ArrayList<>();
    private Long userId;



    public void addProduct(Long productId){
        productIds.add(productId);
    }

    public void removeItem(Long productId) {
        productIds.remove(productId);
    }
}
