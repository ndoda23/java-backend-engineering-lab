package org.example.ticketbookingsystem.service;

import lombok.RequiredArgsConstructor;
import org.example.ticketbookingsystem.domain.Event;
import org.example.ticketbookingsystem.domain.Ticket;
import org.example.ticketbookingsystem.domain.TicketStatus;
import org.example.ticketbookingsystem.dto.event.EventRequest;
import org.example.ticketbookingsystem.dto.event.EventResponse;
import org.example.ticketbookingsystem.dto.event.TicketResponse;
import org.example.ticketbookingsystem.exception.NotFoundException;
import org.example.ticketbookingsystem.repository.EventRepository;
import org.example.ticketbookingsystem.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;

    @Transactional(readOnly = true)
    public List<EventResponse> getUpcomingEvents() {
        return eventRepository.findByEventDateAfter(LocalDateTime.now()).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public EventResponse getById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event not found: " + id));
        return toResponse(event);
    }

    @Transactional(readOnly = true)
    public List<TicketResponse> getAvailableTickets(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException("Event not found: " + eventId);
        }
        return ticketRepository.findByEventIdAndStatus(eventId, TicketStatus.AVAILABLE).stream()
                .map(this::toTicketResponse)
                .toList();
    }

    @Transactional
    public EventResponse create(EventRequest request) {
        Event event = Event.builder()
                .title(request.getTitle().trim())
                .description(request.getDescription())
                .eventDate(request.getEventDate())
                .location(request.getLocation().trim())
                .basePrice(request.getBasePrice())
                .build();

        Event saved = eventRepository.save(event);
        generateTickets(saved, request.getRows(), request.getSeatsPerRow());
        return toResponse(saved);
    }

    private void generateTickets(Event event, int rows, int seatsPerRow) {
        List<Ticket> tickets = new ArrayList<>(rows * seatsPerRow);

        for (int rowIndex = 0; rowIndex < rows; rowIndex++) {
            String seatRow = String.valueOf((char) ('A' + rowIndex));
            for (int seatNumber = 1; seatNumber <= seatsPerRow; seatNumber++) {
                tickets.add(Ticket.builder()
                        .event(event)
                        .seatRow(seatRow)
                        .seatNumber(seatNumber)
                        .price(event.getBasePrice())
                        .status(TicketStatus.AVAILABLE)
                        .build());
            }
        }

        ticketRepository.saveAll(tickets);
    }

    private EventResponse toResponse(Event event) {
        Long eventId = event.getId();
        return EventResponse.builder()
                .id(eventId)
                .title(event.getTitle())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .location(event.getLocation())
                .basePrice(event.getBasePrice())
                .totalTickets(ticketRepository.countByEventId(eventId))
                .availableTickets(ticketRepository.countByEventIdAndStatus(eventId, TicketStatus.AVAILABLE))
                .build();
    }

    private TicketResponse toTicketResponse(Ticket ticket) {
        return TicketResponse.builder()
                .id(ticket.getId())
                .eventId(ticket.getEvent().getId())
                .seatRow(ticket.getSeatRow())
                .seatNumber(ticket.getSeatNumber())
                .price(ticket.getPrice())
                .status(ticket.getStatus())
                .build();
    }
}
