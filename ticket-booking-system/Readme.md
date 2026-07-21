# Ticket Booking System
REST API for event ticket booking with JWT auth, concurrent seat holds, and async confirmation notifications via RabbitMQ.
Built as a portfolio project to practice modern Spring Boot backend patterns.
## Features
- User registration & login with JWT
- Role-based access (`USER` / `ADMIN`)
- Event management (admin creates events + seat map)
- Ticket hold → confirm / cancel flow
- Concurrent booking safety (conditional DB updates + optimistic locking)
- RabbitMQ event on booking confirmation (email simulation)
- Flyway migrations + PostgreSQL
- API versioning (`/api/v1/...`)
- Integration & domain tests
## Tech Stack
| Layer | Technology |
|-------|------------|
| Language | Java 17 |
| Framework | Spring Boot 4.1 |
| Security | Spring Security + JWT |
| Persistence | Spring Data JPA + Hibernate |
| Database | PostgreSQL 16 |
| Migrations | Flyway |
| Messaging | RabbitMQ (AMQP) |
| Tests | JUnit 5, MockMvc, H2 |
| Infra | Docker Compose |
## Architecture
```
Controller → Service → Repository → PostgreSQL
                 ↓
         BookingEventPublisher → RabbitMQ → NotificationListener
```
Layered design with DTOs at the API boundary and domain logic on entities (`Booking.confirm()`, `Ticket.markPending()`, etc.).
## Booking Flow
1. **Register / Login** → receive JWT
2. **Admin** creates an event (rows × seats → tickets)
3. **User** lists available tickets
4. **Hold** seats (`PENDING`, time-limited)
5. **Confirm** → tickets `BOOKED`, message published to RabbitMQ
6. Or **Cancel** → seats released
Two users cannot hold the same seat: status update is conditional (`AVAILABLE → PENDING`).
## API Overview
| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| `POST` | `/api/v1/auth/register` | — | Register |
| `POST` | `/api/v1/auth/login` | — | Login |
| `GET` | `/api/v1/events` | JWT | List events |
| `GET` | `/api/v1/events/{id}` | JWT | Event details |
| `GET` | `/api/v1/events/{id}/tickets/available` | JWT | Available seats |
| `POST` | `/api/v1/events` | ADMIN | Create event |
| `POST` | `/api/v1/bookings` | JWT | Hold tickets |
| `POST` | `/api/v1/bookings/{id}/confirm` | JWT | Confirm booking |
| `POST` | `/api/v1/bookings/{id}/cancel` | JWT | Cancel booking |
| `GET` | `/api/v1/bookings/my` | JWT | My bookings |
| `GET` | `/api/v1/bookings/{id}` | JWT | Booking by id |
Protected requests need:
```http
Authorization: Bearer <token>
```
## Quick Start
### Prerequisites
- Java 17+
- Maven (or `./mvnw`)
- Docker Desktop
### 1. Start infrastructure
```bash
# Postgres only
docker compose up -d postgres
# Postgres + RabbitMQ + Redis
docker compose --profile full up -d
```
RabbitMQ UI: [http://localhost:15672](http://localhost:15672) (`guest` / `guest`)
### 2. Configure DB (if needed)
`src/main/resources/application.properties` — default expects local Postgres:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ticketbooking
spring.datasource.username=postgres
spring.datasource.password=
```
Align the password with your local Postgres or Docker (`postgres` in `docker-compose.yml`).
### 3. Run the app
```bash
./mvnw spring-boot:run
```
Or run `TicketBookingSystemApplication` from IntelliJ.
### 4. Run tests
```bash
./mvnw test
```
Tests use the `test` profile (H2 in-memory, RabbitMQ disabled, no-op publisher).
## Project Structure
```
src/main/java/.../
├── controller/     # REST endpoints
├── service/        # Business logic
├── repository/     # Spring Data JPA
├── domain/         # Entities & enums
├── dto/            # Request/response models
├── security/       # JWT filter & SecurityConfig
├── messaging/      # RabbitMQ publisher & listener
├── config/         # RabbitMQ beans
└── exception/      # Global error handling
```
## Notes
- Without RabbitMQ the app still starts: a no-op publisher is used; with RabbitMQ available, messages are published for real.
- Pending holds expire after `app.booking.hold-minutes` (default: 10).
- Schema is owned by Flyway (`V1__init_schema.sql`); Hibernate validates only.
