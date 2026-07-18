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

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
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

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
