package org.example.ticketbookingsystem.dto.booking;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class CreateBookingRequest {

    @NotEmpty(message = "At least one ticket id is required")
    private List<Long> ticketIds;
}
