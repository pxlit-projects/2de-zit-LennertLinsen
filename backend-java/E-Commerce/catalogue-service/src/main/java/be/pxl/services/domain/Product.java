package be.pxl.services.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="products")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String productName;
    private String description;
    @ManyToOne
    private Category category;
    private int stock;
    private double price;
    private String supplier;
    private int wishlisted;

    public Product(String productName, String description, int stock, double price, String supplier){
        this.productName = productName;
        this.description = description;
        this.stock = stock;
        this.price = price;
        this.supplier = supplier;
        this.wishlisted = 0;
    }

    public void addToCounter() {
        wishlisted += 1;
    }
}
