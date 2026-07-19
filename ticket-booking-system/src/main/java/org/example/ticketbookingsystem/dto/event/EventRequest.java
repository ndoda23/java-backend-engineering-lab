package org.example.ticketbookingsystem.dto.event;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Event date is required")
    private LocalDateTime eventDate;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Base price is required")
    @Positive(message = "Base price must be positive")
    private Double basePrice;

    /** How many seat rows to generate (A, B, C, ...). */
    @NotNull(message = "Rows is required")
    @Min(value = 1, message = "At least 1 row is required")
    @Max(value = 26, message = "Maximum 26 rows (A-Z) allowed")
    private Integer rows;

    /** How many seats in each row. */
    @NotNull(message = "Seats per row is required")
    @Min(value = 1, message = "At least 1 seat per row is required")
    @Max(value = 50, message = "Maximum 50 seats per row allowed")
    private Integer seatsPerRow;
}
