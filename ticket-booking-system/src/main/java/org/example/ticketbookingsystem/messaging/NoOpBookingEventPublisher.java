package org.example.ticketbookingsystem.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NoOpBookingEventPublisher implements BookingEventPublisher {

    @Override
    public void publishBookingConfirmed(BookingConfirmedEvent event) {
        log.debug("RabbitMQ unavailable; skipped booking confirmed event for bookingId={}", event.getBookingId());
    }
}
