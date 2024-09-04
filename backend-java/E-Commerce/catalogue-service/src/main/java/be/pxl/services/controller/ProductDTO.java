package be.pxl.services.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private String productName;
    private String description;
    private Long categoryId;
    private int stock;
    private double price;
    private String supplier;
}
