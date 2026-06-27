package org.example.springeshopapi.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDTO {
    private String title;
    private String description;
    private BigDecimal price;
    private int stock;
    private String imageUrl;
}
