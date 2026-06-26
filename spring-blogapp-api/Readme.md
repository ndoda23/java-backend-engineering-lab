# Spring Boot Blog API

A clean and efficient RESTful Blog API built using Spring Boot and Spring Data JPA. This project implements full CRUD functionality for managing users and their blog posts, data validation, and relational database mapping.

## Tech Stack
* Java 17
* Spring Boot (Web, Data JPA, Validation)
* PostgreSQL (Database)
* Maven (Dependency Management)

## Features and Architecture
* Full CRUD Operations: Create, Read, Update, and Delete capabilities for both Users and Posts.
* Data Validation (Jakarta Validation): Protects the database from invalid or empty data (e.g., unique email/username checks, minimum post length constraints).
* Database Relationships: Robust ManyToOne relationship mapping between Posts and Users using a user_id Foreign Key.

## API Endpoints

### Users
* POST /api/users - Register a new user (with active validation)
* GET /api/users - Retrieve a list of all registered users

### Posts
* POST /api/posts/user/{userId} - Create a new blog post for a specific user
* GET /api/posts - Fetch all blog posts
* PUT /api/posts/{postId} - Update the title or content of an existing post
* DELETE /api/posts/{postId} - Delete a specific post by its ID

## How to Run Locally
1. Clone the repository.
2. Update the src/main/resources/application.properties file with your local PostgreSQL credentials (url, username, password).
3. Run the application from your IDE or via terminal using: mvn spring-boot:run.
