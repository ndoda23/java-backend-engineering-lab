package org.example.ticketbookingsystem.messaging;

public interface BookingEventPublisher {

    void publishBookingConfirmed(BookingConfirmedEvent event);
}
