package be.pxl.services.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateDTO {
    private String productName;
    private String description;
    private double price;
    private Long categoryId;
    private int stock;
    private String supplier;
}
