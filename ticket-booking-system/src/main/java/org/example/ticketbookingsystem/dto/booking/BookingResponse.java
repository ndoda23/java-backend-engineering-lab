package org.example.ticketbookingsystem.dto.booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.ticketbookingsystem.domain.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {
    private Long id;
    private BookingStatus status;
    private Double totalPrice;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private List<BookingTicketResponse> tickets;
}
