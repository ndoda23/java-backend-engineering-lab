package org.example.ticketbookingsystem.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BookingDomainTest {

    @Test
    void confirm_marksBookingAndTicketsAsConfirmedAndBooked() {
        Ticket ticket = availableTicket();
        ticket.markPending();

        Booking booking = pendingBooking(ticket);

        booking.confirm();

        assertThat(booking.getStatus()).isEqualTo(BookingStatus.CONFIRMED);
        assertThat(booking.getExpiresAt()).isNull();
        assertThat(ticket.getStatus()).isEqualTo(TicketStatus.BOOKED);
    }

    @Test
    void cancel_releasesTicketsBackToAvailable() {
        Ticket ticket = availableTicket();
        ticket.markPending();

        Booking booking = pendingBooking(ticket);

        booking.cancel();

        assertThat(booking.getStatus()).isEqualTo(BookingStatus.CANCELLED);
        assertThat(booking.getTickets()).isEmpty();
        assertThat(ticket.getStatus()).isEqualTo(TicketStatus.AVAILABLE);
        assertThat(ticket.getBooking()).isNull();
    }

    @Test
    void confirm_whenAlreadyConfirmed_throws() {
        Ticket ticket = availableTicket();
        ticket.markPending();

        Booking booking = pendingBooking(ticket);
        booking.confirm();

        assertThatThrownBy(booking::confirm)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Only pending bookings");
    }

    private Booking pendingBooking(Ticket ticket) {
        Booking booking = Booking.builder()
                .user(User.builder().email("user@test.com").passwordHash("hash").firstName("Nika").lastName("Test").build())
                .totalPrice(50.0)
                .status(BookingStatus.PENDING)
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .build();
        booking.addTicket(ticket);
        return booking;
    }

    private Ticket availableTicket() {
        Event event = Event.builder()
                .title("Test")
                .eventDate(LocalDateTime.now().plusDays(1))
                .location("Arena")
                .basePrice(50.0)
                .build();

        return Ticket.builder()
                .event(event)
                .seatRow("A")
                .seatNumber(1)
                .price(50.0)
                .status(TicketStatus.AVAILABLE)
                .build();
    }
}
