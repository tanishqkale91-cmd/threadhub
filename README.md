# ThreadHub — Community Discussion Platform

ThreadHub is a Reddit-inspired community discussion platform built with Spring Boot, PostgreSQL, React, Vite, and Tailwind CSS. It provides the foundation for users to create accounts, authenticate securely, create and discover communities, join or leave communities, and manage community membership.

---

## Live Deployment

- **Frontend:** https://threadhub-silk.vercel.app
- **Backend API:** https://threadhub-production.up.railway.app
- **Repository:** https://github.com/tanishqkale91-cmd/threadhub

---

## 1. Application Architecture

```
threadhub/
├── .github/workflows/ci.yml       # GitHub Actions CI pipeline
├── frontend/                       # React 19 + Vite 8 + Tailwind CSS v4 frontend
│   ├── src/
│   │   ├── api/                   # API client layer (authApi, communityApi, userApi)
│   │   ├── components/            # Reusable UI components & layouts
│   │   ├── context/               # AuthContext for session management
│   │   └── pages/                 # Route components (Home, Communities, Auth)
│   ├── package.json
│   └── vite.config.js
└── threadhub/                      # Java 25 + Spring Boot 4.1.1 backend
    ├── src/main/java/com/threadhub/
    │   ├── config/                # SecurityConfig & CORS configuration
    │   ├── controller/            # Auth, User, and Community REST controllers
    │   ├── dto/                   # Request/Response contracts & Bean Validation
    │   ├── exception/             # GlobalExceptionHandler & custom domain exceptions
    │   ├── model/                 # JPA Entities (User, Community, CommunityMember)
    │   ├── repository/            # Spring Data JPA repositories
    │   ├── security/              # JwtService & SecurityUtils helpers
    │   └── service/               # Core business logic services
    ├── pom.xml
    └── application.properties
```

---

## 2. Authentication & Authorization Model

ThreadHub uses **Spring Security** with **OAuth2 Resource Server** and **JWT (JSON Web Tokens)** signed using HMAC-SHA256 (`HS256`).

- **Authentication Flow**:
  1. Clients authenticate via `POST /api/auth/login` with email and password.
  2. The server returns a JWT access token valid for 24 hours (86,400,000 ms).
  3. Clients attach the token to subsequent requests using the standard header: `Authorization: Bearer <token>`.

- **Access Policy**:
  - **Public Endpoints**:
    - `POST /api/users` (User registration)
    - `POST /api/auth/login` (User login)
    - `GET /api/communities` (List all communities)
    - `GET /api/communities/{id}` (Get community details)
    - `GET /api/communities/name/{name}` (Get community by name)
    - `GET /api/communities/{id}/members` (List community members)
  - **Protected Endpoints** (Requires valid Bearer token):
    - `GET /api/users/{id}` (User profile lookup)
    - `POST /api/communities` (Create new community)
    - `POST /api/communities/{id}/join` (Join community)
    - `DELETE /api/communities/{id}/leave` (Leave community)
    - `GET /api/communities/{id}/membership` (Get current user membership status)

---

## 3. Environment Variables & Setup

### Backend Environment Variables (`./threadhub/.env`)

| Variable Name | Description | Example / Default |
|---------------|-------------|-------------------|
| `DB_URL` | PostgreSQL JDBC connection URL | `jdbc:postgresql://localhost:5432/threadhub` |
| `DB_USERNAME` | PostgreSQL database user | `threadhub_user` |
| `DB_PASSWORD` | PostgreSQL database password | `threadhub_dev` |
| `JWT_SECRET` | 256-bit (32+ byte) secret key | `404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970` |

### Frontend Environment Variables (`./frontend/.env`)

```env
VITE_API_BASE_URL=http://localhost:8080
```

---

## 4. How to Run & Test

### Backend (Spring Boot)
```bash
cd threadhub

# Run test suite
DB_URL=jdbc:postgresql://localhost:5432/threadhub \
DB_USERNAME=threadhub_user \
DB_PASSWORD=threadhub_dev \
JWT_SECRET=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970 \
./mvnw test

# Start backend server
DB_URL=jdbc:postgresql://localhost:5432/threadhub \
DB_USERNAME=threadhub_user \
DB_PASSWORD=threadhub_dev \
JWT_SECRET=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970 \
./mvnw spring-boot:run
```

### Frontend (React / Vite)
```bash
cd frontend

# Install dependencies
npm install

# Run Oxlint linter
npm run lint

# Build production bundle
npm run build

# Start dev server
npm run dev
```

---

## 5. Standard Error Response Convention

All application-level API errors return a standardized `ErrorResponse` payload:

```json
{
  "timestamp": "2026-10-10T12:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "details": [
    "Username must be between 3 and 50 characters",
    "Invalid email format"
  ]
}
```

### Standard Status Codes
- `400 Bad Request`: Validation failure, malformed JSON body, invalid path parameter type, business rule violation.
- `401 Unauthorized`: Missing authentication, expired token, invalid token signature, invalid login credentials.
- `403 Forbidden`: Access denied.
- `404 Not Found`: User or Community resource not found.
- `409 Conflict`: Duplicate username, duplicate email, duplicate community name, or already a member.
- `500 Internal Server Error`: Generic unhandled server-side failure (log trace suppressed from client output).

---

## 6. Guidelines for External Contributors

When implementing reserved issues:
- **Issue #1**: Post CRUD APIs
- **Issue #2**: Home and Community Feeds
- **Issue #3**: Comments & Replies
- **Issue #4**: Post Voting
- **Issue #5**: User Profiles

**Rules**:
1. Keep controllers thin; execute business logic in transaction-scoped `@Service` classes.
2. Define request validation rules on DTO classes (`@Valid @RequestBody`), never directly on entity objects.
3. Derive identity from `SecurityUtils.getCurrentUserId()` rather than accepting trusted client user IDs.
4. Ensure all new endpoints register appropriate exception handling in `GlobalExceptionHandler`.
5. Maintain unit and integration test coverage for both success and error paths.

---

## Technology Stack

| Layer | Technology |
|---|---|
| Frontend | React 19 |
| Build Tool | Vite 8 |
| Styling | Tailwind CSS v4 |
| Routing | React Router v7 |
| Backend | Spring Boot 4.1.1 |
| Language | Java 25 |
| Security | Spring Security + JWT |
| ORM | Spring Data JPA + Hibernate |
| Database | PostgreSQL |
| Production DB | Neon PostgreSQL |
| Backend Hosting | Railway |
| Frontend Hosting | Vercel |
| Build System | Maven |
| CI Pipeline | GitHub Actions |
