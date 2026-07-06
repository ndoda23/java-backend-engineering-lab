package org.example.springordernotification.notification;

import org.example.springordernotification.order.OrderEvent;
import org.example.springordernotification.order.OrderStatus;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationListener {

    @RabbitListener(queues = "order.queue")
    public void handleOrderEvent(OrderEvent event) {
        String message = resolveMessage(event.getStatus());
        System.out.println("📧 Notification received!");
        System.out.println("Order ID: " + event.getOrderId());
        System.out.println("Customer: " + event.getCustomerEmail());
        System.out.println("✉️ " + message + event.getCustomerEmail());
    }

    private String resolveMessage(OrderStatus status) {
        return switch (status) {
            case ORDER_CREATED -> "Thank you for your order! ";
            case ORDER_SHIPPED -> "Your order is on the way! ";
            case ORDER_CANCELLED -> "Your order was cancelled. ";
        };
    }
}