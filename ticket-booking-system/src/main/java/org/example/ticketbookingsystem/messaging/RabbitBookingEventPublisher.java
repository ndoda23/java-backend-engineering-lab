package org.example.ticketbookingsystem.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.ticketbookingsystem.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Primary
@RequiredArgsConstructor
@ConditionalOnBean(RabbitTemplate.class)
public class RabbitBookingEventPublisher implements BookingEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publishBookingConfirmed(BookingConfirmedEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                event
        );
        log.info("Published booking confirmed event for bookingId={}", event.getBookingId());
    }
}
