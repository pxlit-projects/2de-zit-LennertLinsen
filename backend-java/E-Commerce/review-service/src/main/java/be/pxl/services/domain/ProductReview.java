package be.pxl.services.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name="productreviews")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductReview {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private String description;
    private int stars;
    private LocalDateTime reviewTime;
    private Long productId;
    private String supplier;

    public ProductReview(String title, String description, int stars, String supplier, Long productId){
        this.title = title;
        this.description = description;
        this.stars = stars;
        this.reviewTime = LocalDateTime.now();
        this.productId = productId;
        this.supplier = supplier;
    }
}
