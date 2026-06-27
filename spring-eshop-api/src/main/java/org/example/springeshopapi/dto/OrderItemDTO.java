package org.example.springeshopapi.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDTO {
    private String productTitle;
    private int quantity;
    private BigDecimal price;
}
