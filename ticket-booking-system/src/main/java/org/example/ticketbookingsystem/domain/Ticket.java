package org.example.ticketbookingsystem.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "tickets",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_ticket_event_seat",
                columnNames = {"event_id", "seat_row", "seat_number"}
        )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @Column(name = "seat_row", nullable = false)
    private String seatRow;

    @Column(name = "seat_number", nullable = false)
    private Integer seatNumber;

    @Column(nullable = false)
    private Double price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private TicketStatus status = TicketStatus.AVAILABLE;

    @Version
    private Long version;

    public void markPending() {
        if (status != TicketStatus.AVAILABLE) {
            throw new IllegalStateException("Only available tickets can be held");
        }
        this.status = TicketStatus.PENDING;
    }

    public void markBooked() {
        if (status != TicketStatus.PENDING) {
            throw new IllegalStateException("Only pending tickets can be booked");
        }
        this.status = TicketStatus.BOOKED;
    }

    public void release() {
        this.status = TicketStatus.AVAILABLE;
        this.booking = null;
    }
}
