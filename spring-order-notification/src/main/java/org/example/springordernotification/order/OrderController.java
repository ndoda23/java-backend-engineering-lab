package org.example.springordernotification.order;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderPublisher orderPublisher;

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody OrderEvent event) {
        event.setCreatedAt(LocalDateTime.now());
        orderPublisher.publishOrderEvent(event);
        return ResponseEntity.ok("Order created and event published!");
    }
}