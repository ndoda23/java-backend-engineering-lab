package org.example.ticketbookingsystem.messaging;

import lombok.extern.slf4j.Slf4j;
import org.example.ticketbookingsystem.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnBean(RabbitTemplate.class)
public class BookingNotificationListener {

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void handleBookingConfirmed(BookingConfirmedEvent event) {
        log.info("Notification: booking #{} confirmed for {} ({} ticket(s), total ${})",
                event.getBookingId(),
                event.getUserEmail(),
                event.getTicketCount(),
                event.getTotalPrice());
        log.info("Email would be sent: Your booking #{} is confirmed. Total: ${}",
                event.getBookingId(),
                event.getTotalPrice());
    }
}
