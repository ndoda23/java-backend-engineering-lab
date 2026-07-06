# Spring Order Notification System

A Spring Boot application demonstrating event-driven architecture using RabbitMQ as a message broker.

When an order is created via REST API, an event is published to RabbitMQ and consumed by a notification listener that simulates sending an email to the customer.

## Tech Stack

- Java 17
- Spring Boot 4.1.0
- Spring AMQP (RabbitMQ)
- Lombok

## Run Locally

Start RabbitMQ:
```bash
docker start rabbitmq
```

Run the application:
```bash
./mvnw spring-boot:run
```

Test with Postman:
```
POST http://localhost:8080/api/orders
Content-Type: application/json

{
  "orderId": 1,
  "customerEmail": "test@gmail.com",
  "status": "CREATED"
}
```
