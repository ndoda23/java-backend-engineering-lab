package org.example.springeshopapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.springeshopapi.dto.OrderDTO;
import org.example.springeshopapi.dto.OrderRequest;
import org.example.springeshopapi.model.Order;
import org.example.springeshopapi.model.OrderStatus;
import org.example.springeshopapi.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
        OrderDTO createdOrderDto = orderService.createOrder(orderRequest);
        return new ResponseEntity<>(createdOrderDto, HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDTO>> getOrderHistory(@PathVariable Long userId){
        List<OrderDTO> history = orderService.getOrderHistory(userId);
        return ResponseEntity.ok(history);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable Long id,
                                                      @RequestParam OrderStatus status){
        OrderDTO updatedOrder = orderService.updateOrderStatus(id,status);
        return  ResponseEntity.ok(updatedOrder);
    }
}
