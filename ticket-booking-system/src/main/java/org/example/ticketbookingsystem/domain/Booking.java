package org.example.ticketbookingsystem.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Ticket> tickets = new ArrayList<>();

    @Column(nullable = false)
    private Double totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private BookingStatus status = BookingStatus.PENDING;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    public void addTicket(Ticket ticket) {
        tickets.add(ticket);
        ticket.setBooking(this);
    }

    public void removeTicket(Ticket ticket) {
        tickets.remove(ticket);
        ticket.setBooking(null);
    }

    public void confirm() {
        if (status != BookingStatus.PENDING) {
            throw new IllegalStateException("Only pending bookings can be confirmed");
        }
        if (expiresAt != null && expiresAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Booking hold has expired");
        }
        this.status = BookingStatus.CONFIRMED;
        this.expiresAt = null;
        tickets.forEach(Ticket::markBooked);
    }

    public void cancel() {
        if (status == BookingStatus.CANCELLED || status == BookingStatus.EXPIRED) {
            throw new IllegalStateException("Booking is already closed");
        }
        if (status == BookingStatus.CONFIRMED) {
            throw new IllegalStateException("Confirmed bookings cannot be cancelled in this version");
        }
        this.status = BookingStatus.CANCELLED;
        this.expiresAt = null;
        releaseAllTickets();
    }

    public void markExpired() {
        if (status != BookingStatus.PENDING) {
            return;
        }
        this.status = BookingStatus.EXPIRED;
        this.expiresAt = null;
        releaseAllTickets();
    }

    private void releaseAllTickets() {
        for (Ticket ticket : new ArrayList<>(tickets)) {
            ticket.release();
        }
        tickets.clear();
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
