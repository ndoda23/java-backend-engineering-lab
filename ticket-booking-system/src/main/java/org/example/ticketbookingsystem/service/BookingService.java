package org.example.ticketbookingsystem.service;

import lombok.RequiredArgsConstructor;
import org.example.ticketbookingsystem.domain.Booking;
import org.example.ticketbookingsystem.domain.BookingStatus;
import org.example.ticketbookingsystem.domain.Ticket;
import org.example.ticketbookingsystem.domain.TicketStatus;
import org.example.ticketbookingsystem.domain.User;
import org.example.ticketbookingsystem.dto.booking.BookingResponse;
import org.example.ticketbookingsystem.dto.booking.BookingTicketResponse;
import org.example.ticketbookingsystem.dto.booking.CreateBookingRequest;
import org.example.ticketbookingsystem.exception.ConflictException;
import org.example.ticketbookingsystem.exception.ForbiddenException;
import org.example.ticketbookingsystem.exception.NotFoundException;
import org.example.ticketbookingsystem.repository.BookingRepository;
import org.example.ticketbookingsystem.repository.TicketRepository;
import org.example.ticketbookingsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    @Value("${app.booking.hold-minutes:10}")
    private long holdMinutes;

    @Transactional
    public BookingResponse createHold(String email, CreateBookingRequest request) {
        User user = getUser(email);
        List<Long> ticketIds = normalizeTicketIds(request.getTicketIds());

        List<Ticket> tickets = ticketRepository.findAllById(ticketIds);
        if (tickets.size() != ticketIds.size()) {
            throw new NotFoundException("One or more tickets were not found");
        }

        Long eventId = tickets.get(0).getEvent().getId();
        boolean sameEvent = tickets.stream()
                .allMatch(ticket -> Objects.equals(ticket.getEvent().getId(), eventId));
        if (!sameEvent) {
            throw new ConflictException("All tickets in one booking must belong to the same event");
        }

        int updated = ticketRepository.updateTicketStatusSecurely(
                ticketIds,
                TicketStatus.PENDING,
                TicketStatus.AVAILABLE
        );
        if (updated != ticketIds.size()) {
            throw new ConflictException("One or more tickets are no longer available");
        }

        // Reload after bulk update so persistence context matches DB
        tickets = ticketRepository.findAllById(ticketIds);

        double totalPrice = tickets.stream().mapToDouble(Ticket::getPrice).sum();

        Booking booking = Booking.builder()
                .user(user)
                .status(BookingStatus.PENDING)
                .totalPrice(totalPrice)
                .expiresAt(LocalDateTime.now().plusMinutes(holdMinutes))
                .build();

        for (Ticket ticket : tickets) {
            booking.addTicket(ticket);
        }

        return toResponse(bookingRepository.save(booking));
    }

    @Transactional
    public BookingResponse confirm(String email, Long bookingId) {
        Booking booking = getOwnedBooking(email, bookingId);

        if (booking.getStatus() == BookingStatus.PENDING
                && booking.getExpiresAt() != null
                && booking.getExpiresAt().isBefore(LocalDateTime.now())) {
            booking.markExpired();
            bookingRepository.save(booking);
            throw new ConflictException("Booking hold has expired");
        }

        try {
            booking.confirm();
        } catch (IllegalStateException ex) {
            throw new ConflictException(ex.getMessage());
        }

        return toResponse(bookingRepository.save(booking));
    }

    @Transactional
    public BookingResponse cancel(String email, Long bookingId) {
        Booking booking = getOwnedBooking(email, bookingId);
        try {
            booking.cancel();
        } catch (IllegalStateException ex) {
            throw new ConflictException(ex.getMessage());
        }
        return toResponse(bookingRepository.save(booking));
    }

    @Transactional(readOnly = true)
    public List<BookingResponse> getMyBookings(String email) {
        User user = getUser(email);
        return bookingRepository.findByUserIdOrderByCreatedAtDesc(user.getId()).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public BookingResponse getById(String email, Long bookingId) {
        return toResponse(getOwnedBooking(email, bookingId));
    }

    private Booking getOwnedBooking(String email, Long bookingId) {
        User user = getUser(email);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found: " + bookingId));

        if (!Objects.equals(booking.getUser().getId(), user.getId())) {
            throw new ForbiddenException("You do not own this booking");
        }
        return booking;
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found: " + email));
    }

    private List<Long> normalizeTicketIds(List<Long> ticketIds) {
        Set<Long> unique = new HashSet<>(ticketIds);
        if (unique.size() != ticketIds.size()) {
            throw new ConflictException("Duplicate ticket ids are not allowed");
        }
        return List.copyOf(unique);
    }

    private BookingResponse toResponse(Booking booking) {
        List<BookingTicketResponse> tickets = booking.getTickets().stream()
                .map(ticket -> BookingTicketResponse.builder()
                        .ticketId(ticket.getId())
                        .eventId(ticket.getEvent().getId())
                        .seatRow(ticket.getSeatRow())
                        .seatNumber(ticket.getSeatNumber())
                        .price(ticket.getPrice())
                        .status(ticket.getStatus())
                        .build())
                .toList();

        return BookingResponse.builder()
                .id(booking.getId())
                .status(booking.getStatus())
                .totalPrice(booking.getTotalPrice())
                .createdAt(booking.getCreatedAt())
                .expiresAt(booking.getExpiresAt())
                .tickets(tickets)
                .build();
    }
}
