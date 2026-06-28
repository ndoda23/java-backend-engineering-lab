package org.example.springeshopapi.service;

import org.example.springeshopapi.dto.OrderDTO;
import org.example.springeshopapi.dto.OrderRequest;
import org.example.springeshopapi.model.OrderStatus;

import java.util.List;

public interface OrderService {
    OrderDTO createOrder(OrderRequest orderRequest);
    List<OrderDTO> getOrderHistory(Long userId);
    OrderDTO updateOrderStatus(Long orderId, OrderStatus newStatus);
}
