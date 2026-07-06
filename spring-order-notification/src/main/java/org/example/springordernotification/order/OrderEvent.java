package org.example.springordernotification.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEvent implements Serializable {
    private Long orderId;
    private String customerEmail;
    private OrderStatus  status;
    private LocalDateTime createdAt;
}