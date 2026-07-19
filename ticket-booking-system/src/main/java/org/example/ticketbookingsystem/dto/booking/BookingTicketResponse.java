package org.example.ticketbookingsystem.dto.booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.ticketbookingsystem.domain.TicketStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingTicketResponse {
    private Long ticketId;
    private Long eventId;
    private String seatRow;
    private Integer seatNumber;
    private Double price;
    private TicketStatus status;
}
