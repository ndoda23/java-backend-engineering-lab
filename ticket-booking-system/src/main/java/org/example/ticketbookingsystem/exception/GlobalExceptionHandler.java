package org.example.ticketbookingsystem.exception;

import org.example.ticketbookingsystem.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflict(ConflictException ex) {
        return build(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({UnauthorizedException.class, BadCredentialsException.class, UsernameNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleUnauthorized(RuntimeException ex) {
        return build(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().isEmpty()
                ? "Validation failed"
                : ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        return build(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntime(RuntimeException ex) {
        return build(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ErrorResponse> build(String message, HttpStatus status) {
        ErrorResponse body = ErrorResponse.builder()
                .message(message)
                .status(status.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(status).body(body);
    }
}
