package org.example._1globalexceptionhandler.exception;

import java.time.LocalDateTime;

public class ErrorResponse {
    private final boolean success = false;
    private final String message;
    private final LocalDateTime timestamp;

    public ErrorResponse(String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public LocalDateTime getTimestamp() { return timestamp; }
}