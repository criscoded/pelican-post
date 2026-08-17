# Pelican Post
A modern rewrite of my previous Next.js gallery, transitioning to a decoupled architecture with a Spring Boot backend and an Angular frontend. This migration prioritizes enterprise-grade security and modularity.

> Development Context: This project serves as a more secure and scalable alternative to the Next.js implementation, addressing concerns around React Server Component vulnerabilities and managing separated services handed to you with tools like Vercel.

# Architecture Overview

The application is split into two distinct layers:

* Backend (Spring Boot): A robust RESTful API handling business logic, authentication (JWT), data persistence, and security

* Frontend (Angular): A dynamic, client-side interface that consumes the Spring API. The production build is bundled into the backend and served from the same origin.

# Tech Stack

**Backend**

* Spring Boot: Core application framework

* Spring Security + JWT (jjwt): Stateless authentication and authorization

* Spring Data JPA: For streamlined database interactions and ORM

* PostgreSQL: Relational database for persistent storage

* AWS S3 + CloudFront: Image hosting and CDN delivery

* Maven: Dependency management and build automation

**Frontend**

* Angular: Component-based UI development

* Tailwind-inspired custom CSS: Utility styling

# Key Features

* Per-User Isolation: Every user has a private mailbox — images are scoped to their owner server-side, and only the owner can view, upload, or delete them.

* JWT Authentication: Passwords hashed with BCrypt, tokens signed with a configurable secret.

* Decoupled Design: Complete separation of the client and server, allowing for independent scaling and maintenance

* Enhanced Security: Upload validation (image content types only), no internal error leakage, CORS locked to configured origins

* Persistence: Fully integrated PostgreSQL database managed via JPA/Hibernate

# Getting Started

## Local Development

### 1. Start PostgreSQL

```Bash
cd gallery-api
docker compose up -d
```

### 2. Start the backend

```Bash
cd gallery-api
./mvnw spring-boot:run
```

### 3. Start the frontend

```Bash
cd gallery-ui
npm install
npm start
```

The UI runs on http://localhost:4200 and proxies `/api` to the backend on http://localhost:8080.

## Configuration & Environment Variables

These variables allow the application to connect to your database, secure the API, and reach AWS:

| Env Var | Required | Description |
|---|---|---|
| `DB_HOST` | yes | PostgreSQL host (default `localhost`) |
| `DB_PORT` | yes | PostgreSQL port (default `5432`) |
| `DB_DATABASE` | yes | Database name (default `gallery_db`) |
| `DB_USERNAME` | yes | PostgreSQL username (default `gallery`) |
| `DB_PASSWORD` | yes | PostgreSQL password (default `gallery`) |
| `JWT_SECRET` | yes | Long random string used to sign JWTs (**min 32 characters**). Generate with `openssl rand -base64 48` |
| `JWT_EXPIRATION_MS` | no | Token lifetime in ms (default 7 days) |
| `AWS_BUCKET_NAME` | yes | S3 bucket for image storage |
| `AWS_ACCESS_KEY_ID` / `AWS_SECRET_ACCESS_KEY` | yes | AWS credentials |
| `AWS_REGION` | yes | AWS region (e.g. `us-east-1`) |
| `CDN_BASE_URL` | yes | CloudFront (or S3 URL) base for images, e.g. `https://xxxxxxxx.cloudfront.net/` |
| `CORS_ALLOWED_ORIGINS` | no | Comma-separated allowed origins (default `http://localhost:4200,http://localhost:8080`) |
| `PORT` | no | Server port (default `8080`; Render injects this automatically) |

## Running in Production (Docker)

The repo includes a multi-stage Dockerfile that builds the Angular app, bundles it into the Spring Boot jar, and runs a single container:

```Bash
docker build -t pelican-post .
docker run -p 8080:8080 \
  -e DB_HOST=... -e DB_PORT=5432 -e DB_DATABASE=... -e DB_USERNAME=... -e DB_PASSWORD=... \
  -e JWT_SECRET='$(openssl rand -base64 48)' \
  -e AWS_BUCKET_NAME=... -e AWS_ACCESS_KEY_ID=... -e AWS_SECRET_ACCESS_KEY=... -e AWS_REGION=... \
  -e CDN_BASE_URL=https://xxxxxxxx.cloudfront.net/ \
  pelican-post
```

## Deploying to Render

1. Create a repository (GitHub/GitLab) and push this project.

2. In Render, create a **New Blueprint** and point it at your repo. It will detect `render.yaml` and provision:
   - A web service (`pelican-post`) built from the Dockerfile
   - A managed PostgreSQL database (`pelican-post-db`) wired to the service
   - An auto-generated `JWT_SECRET`

3. In the web service environment settings, set the AWS variables (`AWS_BUCKET_NAME`, `AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY`, `AWS_REGION`) and `CDN_BASE_URL`.

4. The app is reachable at `https://pelican-post.onrender.com`. Register an account to get your private mailbox.

# API Documentation

|Method|Endpoint|Description|
|------|--------|-----------|
|POST|/api/auth/register|Create an account (returns JWT)|
|POST|/api/auth/login|Authenticate (returns JWT)|
|GET|/api/images|List your images (Authenticated)|
|GET|/api/images/{id}|Retrieve one of your images (Authenticated)|
|POST|/api/images/upload|Upload a new image (Authenticated)|
|DELETE|/api/images/{id}|Delete one of your images (Authenticated)|

All authenticated endpoints require an `Authorization: Bearer <token>` header. Every response is scoped to the authenticated user — attempting to access another user's image returns 404.
