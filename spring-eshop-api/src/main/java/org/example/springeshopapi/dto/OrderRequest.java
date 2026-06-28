package org.example.springeshopapi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequest
{
    @NotEmpty(message = "order must contain at least one item")
    @Valid
    private List<OrderItemRequest> items;
    @NotNull(message = "user Id is required")
    private Long userId;


    @Data
    public static class OrderItemRequest{
        @NotNull(message = "Product Id is required")
        private Long productId;
        @Min(value = 1,message =  "Quantity must be at least 1")
        private int quantity;
    }

}
