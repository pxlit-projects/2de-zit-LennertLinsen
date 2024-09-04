package be.pxl.services.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="carts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    @Id
    private Long id;
    @CollectionTable
    @OneToMany
    private List<CartItem> cartItems = new ArrayList<>();

    public Cart(Long id){
        this.id = id;
    }
}
