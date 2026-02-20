# Spring-React Gallery
A modern rewrite of my previous T3-based gallery, transitioning to a decoupled architecture with a Spring Boot backend and a React frontend. This migration prioritizes enterprise-grade security and modularity.

> Development Context: This project serves as a more secure and scalable alternative to the t3gallery implementation, addressing concerns around React Server Component vulnerabilities by utilizing a traditional REST API architecture.

# Architecture Overview

The application is split into two distinct layers:

* Backend (Spring Boot): A robust RESTful API handling business logic, data persistence, and security.

* Frontend (React): A dynamic, client-side interface that consumes the Spring API, providing a seamless user experience.

# Tech Stack

**Backend**

* Spring Boot: Core application framework.

* Spring Data JPA: For streamlined database interactions and ORM.

* Spring Security: Enterprise-level authentication and authorization.

* PostgreSQL: Relational database for persistent storage.

* Maven: Dependency management and build automation.

**Frontend**

* React: Component-based UI development.

* Axios: Handling asynchronous HTTP requests to the backend.

* React Router: Managing client-side navigation.

* Tailwind CSS: Utility-first styling for a modern look.

# Key Features

* Decoupled Design: Complete separation of the client and server, allowing for independent scaling and maintenance.

* RESTful API: Clean, documented endpoints for gallery operations (CRUD).

* Enhanced Security: Leveraging Spring Security to safeguard image data and user sessions.

* Persistence: Fully integrated PostgreSQL database managed via JPA/Hibernate.

# Getting Started

**Backend Setup**

1. Navigate to the /backend directory.

2. Configure your PostgreSQL settings in src/main/resources/application.properties.

3. Run the application:


```Bash```
```
./mvnw spring-boot:run
```

**Frontend Setup**

1. Navigate to the /frontend directory.

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

# Why the switch from Next.js?

While Next.js offers excellent speed, the recent security landscape (specifically regarding RSCs) led to this pivot. By using Spring Boot, the project gains:

* Type Safety: Strong typing across the entire backend.

* Mature Ecosystem: Proven libraries for security and database management.

* Better Portability: A clear separation that allows the frontend to be swapped or updated without affecting the core logic.

# API Documentation

The backend exposes a structured REST API. Below are the primary endpoints:

|Method|Endpoint|Description|
|------|--------|-----------|
|GET|/api/images|Fetch all gallery images|
|GET|/api/images/{id}|Retrieve specific image metadata|
|POST|/api/images/upload|Upload a new image (Authenticated)|
|DELETE|/api/images/{id}|Remove an image from the gallery|
|POST|/api/auth/login|User authentication and JWT issuance|
