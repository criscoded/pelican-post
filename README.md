# Pelican Post
A modern rewrite of my previous Next.js gallery, transitioning to a decoupled architecture with a Spring Boot backend and an Angular frontend. This migration prioritizes enterprise-grade security and modularity. 

> Development Context: This project serves as a more secure and scalable alternative to the Next.js implementation, addressing concerns around React Server Component vulnerabilities and avoiding the "eggs in one basket," approach of the Vercel ecosystem.

# Architecture Overview

The application is split into two distinct layers:

* Backend (Spring Boot): A robust RESTful API handling business logic, data persistence, and security

* Frontend (React): A dynamic, client-side interface that consumes the Spring API, providing a seamless user experience

# Tech Stack

**Backend**

* Spring Boot: Core application framework

* Spring Data JPA: For streamlined database interactions and ORM

* Spring Security: Enterprise-level authentication and authorization

* PostgreSQL: Relational database for persistent storage

* Maven: Dependency management and build automation

**Frontend**

* React: Component-based UI development

* Axios: Handling asynchronous HTTP requests to the backend

* React Router: Managing client-side navigation

* Tailwind CSS: Utility-first styling for a modern look

# Key Features

* Decoupled Design: Complete separation of the client and server, allowing for independent scaling and maintenance

* RESTful API: Clean, documented endpoints for gallery operations (CRUD)

* Enhanced Security: Leveraging Spring Security to safeguard image data and user sessions

* Persistence: Fully integrated PostgreSQL database managed via JPA/Hibernate

# Getting Started

## Backend Setup

1. Navigate to the /backend directory

2. Configure your PostgreSQL settings in src/main/resources/application.properties

3. Run the application:


```Bash```
```
./mvnw spring-boot:run
```

## Frontend Setup 

1. Navigate to the /frontend directory

2. Install dependencies:

```Bash```
```
npm install
```

3. Start the development server:

```Bash```
```
npm start
```
## Configuration & Environment Variables
To run this project locally, you will need to set up the following environment variables. You can create a .env file in the frontend directory and an application-local.properties (or use system env vars) for the backend.

***Backend (Spring Boot)***

These variables allow the application to connect to your database and secure the API

* DB_URL: The JDBC connection string (e.g., jdbc:postgresql://localhost:5432/gallery_db)

* DB_USERNAME: Your PostgreSQL username

* DB_PASSWORD: Your PostgreSQL password

* JWT_SECRET: A long, random string used to sign and verify JSON Web Tokens

* AWS_BUCKET: Connection for image hosting services

* CORS Configuration: Ensure that your Spring Boot WebMvcConfigurer is configured to allow requests from your frontend's origin (e.g., http://localhost:5173) during development

# Why the switch from Next.js?

While Next.js offers excellent speed, the recent security landscape (specifically regarding RSCs) led to this pivot. By using Spring Boot, the project gains:

* Type Safety: Strong typing across the entire backend

* Mature Ecosystem: Proven libraries for security and database management

* Better Portability: A clear separation that allows the frontend to be swapped or updated without affecting the core logic
  
* Security Isolation: The backend serves as a strict gatekeeper, mitigating risks associated with server-side execution vulnerabilities

* Maintainability: Following the SOLID principles in Java ensures the codebase remains clean as features grow

* Industry Standard: Demonstrates proficiency in the standard enterprise stack used by large-scale technology companies

# API Documentation

The backend exposes a structured REST API. Below are the primary endpoints:

|Method|Endpoint|Description|
|------|--------|-----------|
|GET|/api/images|Fetch all gallery images|
|GET|/api/images/{id}|Retrieve specific image metadata|
|POST|/api/images/upload|Upload a new image (Authenticated)|
|DELETE|/api/images/{id}|Remove an image from the gallery|
|POST|/api/auth/login|User authentication and JWT issuance|
