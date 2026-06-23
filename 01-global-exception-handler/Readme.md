# Global Exception Handling Architecture 

This module demonstrates a centralized exception handling mechanism in Spring Boot using standard enterprise architectural patterns. It decouples error-handling logic from business services and controllers, ensuring unified and clean API responses across the application.

## Core Components Included

* **`ResourceConflictException`**: A custom runtime exception designed to represent business-logic conflicts, such as overlapping data intervals, double-bookings, or state conflicts.
* **`ErrorResponse`**: A rigid data transfer object (DTO) that defines a consistent contract for error structures sent back to the client. It standardizes fields such as the success status, human-readable message, and timestamp.
* **`GlobalExceptionHandler`**: A central orchestrator annotated with @RestControllerAdvice. It intercepts specified exceptions system-wide, logs the failure context, and returns an appropriate HTTP status code (409 Conflict) with a structured JSON payload.

## Key Benefits Demonstrated

* **Security Hardening**: Prevents leakage of internal technical details or Java stack traces to the public frontend API.
* **Consistent API Contract**: Ensures that clients can always rely on an identical JSON structure whenever a backend error occurs.
* **Cleaner Business Logic**: Eliminates repetitive try-catch blocks and manual response generation inside Spring services and controllers.
