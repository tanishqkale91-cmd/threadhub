# ThreadHub

ThreadHub is a Reddit-inspired community discussion platform built as a full-stack web application. It provides the foundation for users to create accounts, authenticate securely, create and discover communities, join or leave communities, and manage community membership.

The project is designed as a standalone social discussion platform. Posts, comments, voting, feeds, moderation, and other Reddit-style functionality are planned as future improvements.

## Live Deployment

- **Frontend:** https://threadhub-silk.vercel.app
- **Backend API:** https://threadhub-production.up.railway.app
- **Repository:** https://github.com/tanishqkale91-cmd/threadhub

## Current Features

### Authentication & Users
- User registration
- User login
- JWT-based authentication
- Stateless Spring Security authentication
- BCrypt password hashing
- Protected API endpoints
- Persistent authentication on the frontend

### Communities
- Create communities
- View communities
- View individual community details
- Search/retrieve communities by name
- Join communities
- Leave communities
- Automatic owner membership when creating a community
- Owner cannot leave their own community
- View community members
- Community ownership and membership roles

### Frontend
- React + Vite
- Tailwind CSS
- React Router
- Authentication context
- Protected routes
- Community listing and detail pages
- Login and registration pages
- API integration with the Spring Boot backend
- Production deployment on Vercel

### Backend
- Spring Boot 4.1.1
- Java 25
- Spring Security
- Spring Data JPA
- Hibernate
- PostgreSQL
- JWT authentication
- Bean validation
- Maven
- Production deployment on Railway

### Database
- PostgreSQL
- Local development with PostgreSQL
- Production database hosted on Neon PostgreSQL
- JPA/Hibernate schema management

## Architecture

```text
┌─────────────────────────┐
│       React Frontend    │
│     Vite + Tailwind     │
│                         │
│        Vercel           │
└────────────┬────────────┘
             │ HTTPS / REST API
             ▼
┌─────────────────────────┐
│    Spring Boot Backend  │
│                         │
│ Security + JWT + JPA    │
│                         │
│        Railway          │
└────────────┬────────────┘
             │ JDBC
             ▼
┌─────────────────────────┐
│    PostgreSQL Database  │
│                         │
│         Neon            │
└─────────────────────────┘
```

## Project Structure

```text
threadhub/
├── frontend/                 # React + Vite frontend
│   ├── src/
│   │   ├── api/
│   │   ├── components/
│   │   ├── context/
│   │   ├── pages/
│   │   ├── App.jsx
│   │   └── main.jsx
│   └── package.json
│
├── threadhub/                # Spring Boot backend
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/threadhub/
│   │   │   └── resources/
│   │   └── test/
│   ├── pom.xml
│   └── mvnw
│
└── README.md
```

## API Foundation

The current backend exposes functionality around:

- `/api/users`
- `/api/auth/login`
- `/api/communities`
- Community membership endpoints
- Community member endpoints

Authentication uses JWT bearer tokens for protected endpoints.

## Known Issues / Areas to Fix

These are not blockers for the current deployment, but they are the obvious engineering work remaining.

### Backend

- Replace `spring.jpa.hibernate.ddl-auto=update` with a proper database migration strategy such as Flyway or Liquibase.
- Improve API error handling and standardize error response DTOs.
- Add stronger validation rules for usernames, emails, passwords, community names, and community descriptions.
- Review authorization rules for every endpoint as new resources are introduced.
- Add pagination to endpoints that can return large collections.
- Add database indexes where query patterns require them.
- Replace hard-coded configuration values with environment-based configuration where appropriate.
- Disable or reduce SQL logging in production.
- Add production-oriented observability and structured logging.
- Add rate limiting for authentication endpoints.
- Improve automated integration-test coverage against PostgreSQL.

### Frontend

- Improve loading, error, and empty states across all pages.
- Add form-level validation and better API error messages.
- Improve responsive behavior across mobile and desktop.
- Add reusable UI components as the application grows.
- Improve accessibility, keyboard navigation, and semantic HTML.
- Add frontend tests.
- Improve token/session handling and account state recovery.
- Add a production-ready error boundary.

### Deployment

- Add a custom domain.
- Configure stricter production CORS once all required frontend origins are known.
- Add deployment health checks and monitoring.
- Configure separate development, staging, and production environments.
- Add CI checks for tests and builds before merging to `main`.

## Planned Features

Users
  ↓
Communities
  ↓
Posts          ← NEXT
  ↓
Comments
  ↓
Voting
  ↓
Home / Community Feed
  ↓
Profiles
  ↓
Search
  ↓
Notifications
  ↓
Moderation

## Security Improvements

Security is treated as a core part of the project rather than decorative vocabulary for a README.

Planned improvements include:

- Refresh-token based authentication
- Token revocation strategy
- Rate limiting
- Brute-force protection
- Stronger password policies
- Email verification
- Password reset flow
- Account lockout policies
- Security headers
- Input sanitization
- Comprehensive authorization tests
- Audit logging for sensitive operations

## Testing

The backend currently has automated tests covering the implemented user, authentication, and community functionality.

Run backend tests from the backend directory:

```bash
./mvnw clean test
```

Build the frontend:

```bash
cd frontend
npm install
npm run build
```

## Environment Variables

### Backend

```env
DB_URL=jdbc:postgresql://localhost:5432/threadhub
DB_USERNAME=threadhub_user
DB_PASSWORD=your_database_password
JWT_SECRET=your_32_byte_or_longer_secret
```

### Frontend

```env
VITE_API_BASE_URL=http://localhost:8080
```

Do not commit real credentials, database passwords, JWT secrets, or production environment files.

## Development

### Start the Backend

```bash
cd threadhub
./mvnw spring-boot:run
```

The backend runs on:

```text
http://localhost:8080
```

### Start the Frontend

```bash
cd frontend
npm install
npm run dev
```

The frontend runs on:

```text
http://localhost:5173
```

## Technology Stack

| Layer | Technology |
|---|---|
| Frontend | React |
| Build Tool | Vite |
| Styling | Tailwind CSS |
| Routing | React Router |
| Backend | Spring Boot 4.1.1 |
| Language | Java 25 |
| Security | Spring Security + JWT |
| ORM | Spring Data JPA + Hibernate |
| Database | PostgreSQL |
| Local DB | PostgreSQL |
| Production DB | Neon PostgreSQL |
| Backend Hosting | Railway |
| Frontend Hosting | Vercel |
| Build | Maven |
| Version Control | Git + GitHub |

## Roadmap

```text
[x] Project foundation
[x] User registration
[x] Password hashing
[x] JWT authentication
[x] Community creation
[x] Community membership
[x] Frontend authentication
[x] Frontend community UI
[x] Production backend deployment
[x] Production frontend deployment
[ ] Posts
[ ] Comments
[ ] Voting
[ ] User profiles
[ ] Feed system
[ ] Moderation
[ ] Notifications
[ ] Search
[ ] Advanced security
```
### Issues

## License

This project is currently under active development. Add a formal open-source license before treating the repository as a publicly licensed project.
