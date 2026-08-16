# Pelican Post

**Pelican Post** is a full-stack gallery application that transforms user-uploaded images and personalized notes into interactive digital postcards. 

---

## Architecture Overview

The application is built on a decoupled architecture, ensuring strict separation of concerns, independent scalability, and optimized media delivery:

*   **Backend (Spring Boot):** A robust RESTful API that serves as the strict gatekeeper for business logic, relational data persistence, and enterprise-level security.
*   **Frontend (React):** A dynamic, client-side interface that consumes the Spring API to deliver a seamless and responsive user experience.
*   **Content Delivery (AWS):** Media assets are securely stored and served via cloud infrastructure to maximize throughput and minimize time-to-first-byte (TTFB).

---

## Tech Stack

### Backend
*   **Spring Boot:** Core enterprise application framework.
*   **Spring Data JPA & Hibernate:** Streamlined Object-Relational Mapping (ORM) and database interactions.
*   **Spring Security:** Comprehensive authentication, authorization, and session safeguarding.
*   **PostgreSQL:** Robust relational database for persistent state management.
*   **Maven:** Dependency management and automated build execution.

### Frontend
*   **React:** Component-driven user interface development.
*   **Tailwind CSS:** Utility-first styling for a highly responsive, modern layout.
*   **Axios:** Asynchronous HTTP client for seamless API communication.
*   **React Router:** Client-side state and navigation management.

### Cloud & Infrastructure
*   **AWS S3 & Amazon CloudFront:** Optimized cloud storage and Content Delivery Network (CDN) integration for high-speed global media delivery.

---

## Key Features

*   **RESTful API Integration:** Clean, thoroughly documented endpoints managing all gallery CRUD operations.
*   **Optimized Media Delivery:** Integration with AWS S3 and CloudFront to ensure high-throughput image hosting and rapid rendering. 
*   **Decoupled & Scalable Design:** Complete separation between the React client and Java server, allowing for independent deployment pipelines and easier maintenance.
*   **Enterprise-Grade Security:** Leverages Spring Security and JWTs to isolate server-side execution and securely manage user sessions and image data.
*   **Persistent Data Integrity:** Fully integrated PostgreSQL database managed via JPA/Hibernate to ensure reliable data structures and relationships.

---

## Architecture Decisions: The Shift to Spring Boot

While Next.js offers excellent speed, the evolving security landscape—specifically regarding React Server Components (RSCs)—prompted a strategic pivot to a Java-based backend. By migrating to Spring Boot, Pelican Post gains:

*   **Security Isolation:** The backend operates as an independent, strict gatekeeper, significantly mitigating risks associated with server-side execution vulnerabilities.
*   **Ecosystem Maturity:** Access to battle-tested, proven libraries for complex security and relational database management.
*   **Type Safety & Maintainability:** Applying SOLID principles within a strongly typed Java environment ensures the codebase remains robust and clean as the feature set expands.
*   **Enhanced Portability:** A clear architectural boundary that allows the frontend interface to be overhauled or swapped entirely without disrupting core server logic.
*   **Industry Standards:** Aligns the project with the standard, high-performance enterprise stacks utilized by large-scale technology organizations.

---

## Getting Started

### Backend Setup

1. Navigate to the backend directory:
   ```bash
   cd backend
   ```
2. Configure your PostgreSQL and AWS settings in `src/main/resources/application.properties` or `application-local.properties`.
3. Run the Spring Boot application:
   ```bash
   ./mvnw spring-boot:run
   ```

### Frontend Setup 

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```
2. Install the necessary Node dependencies:
   ```bash
   npm install
   ```
3. Start the Vite/Webpack development server:
   ```bash
   npm start
   ```

---

## Configuration & Environment Variables

To run this project locally, configure the following environment variables. Use a `.env` file in the frontend directory and an `application-local.properties` file (or system environment variables) for the backend.

### Backend Requirements

These variables are required to establish database connections, secure the API, and connect to cloud hosting:

*   **`DB_URL`**: The JDBC connection string (e.g., `jdbc:postgresql://localhost:5432/gallery_db`).
*   **`DB_USERNAME`**: Your local or remote PostgreSQL username.
*   **`DB_PASSWORD`**: Your PostgreSQL password.
*   **`JWT_SECRET`**: A highly secure, randomized cryptographic string used to sign and verify JSON Web Tokens.
*   **`AWS_BUCKET`**: The target AWS S3 bucket for hosting user-uploaded image assets.

> **Note on CORS:** Ensure your Spring Boot `WebMvcConfigurer` is properly configured to accept Cross-Origin requests from your frontend's local development port (e.g., `http://localhost:5173`).

---

## API Documentation

The Spring Boot backend exposes a strictly structured REST API. Below are the primary operational endpoints:

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| **GET** | `/api/images` | Fetch the complete collection of gallery images |
| **GET** | `/api/images/{id}` | Retrieve specific metadata for a single image |
| **POST** | `/api/images/upload` | Upload a new image to the gallery *(Requires Authentication)* |
| **DELETE** | `/api/images/{id}` | Permanently remove an image from the database and storage |
| **POST** | `/api/auth/login` | Authenticate a user and issue a secure JWT |
