package be.pxl.services.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String productName;
    private String description;
    private int stock;
    private double price;
    private Long categoryId;
    private String categoryName;
    private String supplier;
    private int wishlisted;
}
