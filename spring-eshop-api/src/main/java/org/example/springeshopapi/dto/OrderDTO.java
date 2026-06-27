package org.example.springeshopapi.dto;

import lombok.Data;
import org.example.springeshopapi.model.OrderStatus;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO
{
    private Long id;
    private String userEmail;
    private List<OrderItemDTO> items;
    private BigDecimal totalPrice;
    private OrderStatus status;
    private LocalDateTime createdAt;
}
