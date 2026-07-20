package org.example.ticketbookingsystem.messaging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingConfirmedEvent implements Serializable {

    private Long bookingId;
    private String userEmail;
    private Double totalPrice;
    private int ticketCount;
    private LocalDateTime confirmedAt;
}
