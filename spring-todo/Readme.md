# Spring Boot Todo API

A simple Todo REST API built with Spring Boot and PostgreSQL.
All endpoints tested with Postman.

## Technologies
- Java 17
- Spring Boot 4.1.0
- Spring Data JPA / Hibernate
- PostgreSQL
- Lombok

## Setup

1. Create PostgreSQL database:
```sql
CREATE DATABASE todo_db;
```

2. Fill in `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/todo_db
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

3. Run the project — Hibernate will create the table automatically

## Endpoints

| Method | URL | Description |
|--------|-----|-------------|
| GET | /api/todos | Get all todos |
| GET | /api/todos/{id} | Get todo by id |
| POST | /api/todos | Create new todo |
| DELETE | /api/todos/{id} | Delete todo |

## Request Example

```json
{
  "title": "First todo",
  "completed": false
}
```
