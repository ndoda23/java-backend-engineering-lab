package org.example.springordernotification.notification;

import org.example.springordernotification.order.OrderEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationListener {

    @RabbitListener(queues = "order.queue")
    public void handleOrderEvent(OrderEvent event) {
        System.out.println("📧 Notification received!");
        System.out.println("Order ID: " + event.getOrderId());
        System.out.println("Customer: " + event.getCustomerEmail());
        System.out.println("Status: " + event.getStatus());
        System.out.println("Created At: " + event.getCreatedAt());
        System.out.println("✉️ Sending email to: " + event.getCustomerEmail());
    }
}