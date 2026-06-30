###  E-Commerce REST API

A robust, secure, and high-performance e-commerce backend application built adhering to Enterprise architecture standards. This project implements the full lifecycle of product management, user authentication, and order processing.

#### 🛠️ Tech Stack & Architecture
* **Spring Boot (Web, Data JPA)** — For efficient REST endpoint management and database abstraction.
* **PostgreSQL & Hibernate** — Reliable data persistence, transaction management, and optimized SQL queries.
* **DTO (Data Transfer Object) Pattern** — Strictly decouples the database entities from the client-facing data layer for enhanced security and flexibility.

####   Security & Authentication
* **Spring Security & JWT (JSON Web Tokens)** — Fully `Stateless` authentication mechanism.
* **Custom JWT Filter** — Intercepts incoming requests, parses/validates tokens, and populates the `SecurityContext`.
* **Password Hashing** — Secure user credential storage using BCrypt strong hashing function.

####   Advanced Features
* **Data Validation (JSR-380)** — Strict input validation using `@Valid` annotations (e.g., email formatting, password strength, required fields) to block malformed data at the controller level.
* **Global Exception Handling** — Features a centralized error handling layer (`@ControllerAdvice`) that intercepts all runtime and validation exceptions, mapping them into clean, structured, and user-friendly JSON responses (`AppErrorResponse`).

####   Core API Endpoints
* `POST /api/auth/register` — User registration with input validation.
* `POST /api/auth/login` — Authentication that generates and returns the JWT token.
* `GET /api/products` — Fetches the product catalog (Secured via Spring Security).
* `POST /api/orders` — Places a new order and binds it to the authenticated user.
