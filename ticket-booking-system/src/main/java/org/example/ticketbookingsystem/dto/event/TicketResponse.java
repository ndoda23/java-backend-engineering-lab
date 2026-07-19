package org.example.ticketbookingsystem.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.ticketbookingsystem.domain.TicketStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class
TicketResponse {
    private Long id;
    private Long eventId;
    private String seatRow;
    private Integer seatNumber;
    private Double price;
    private TicketStatus status;
}
