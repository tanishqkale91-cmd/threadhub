# Contributing to ThreadHub

Thank you for your interest in contributing to **ThreadHub**! We welcome contributions from the community to help make ThreadHub a robust, safe, and modern discussion platform.

Please read through these guidelines before submitting bug reports, feature requests, or Pull Requests (PRs).

---

## 1. Scope of Contributions & Reserved Issues

ThreadHub follows a structured roadmap. Certain core product features are deliberately reserved for dedicated GitHub issues:

- **Issue #1**: Post CRUD APIs (Create, Read, Update, Delete posts)
- **Issue #2**: Home and Community Feeds (Aggregated post feeds)
- **Issue #3**: Comments & Replies (Nested discussion threads)
- **Issue #4**: Post Voting (Upvoting and downvoting mechanism)
- **Issue #5**: User Profiles & Settings

> **Note**: Please do not submit unsolicited PRs that pre-implement these features without an assigned issue ticket. Keep PRs focused, reviewable, and aligned with open issues.

---

## 2. Getting Started

### Prerequisites
- **Java**: Version 25 (OpenJDK / Temurin)
- **Maven**: Version 3.9+ (or use `./mvnw`)
- **Node.js**: Version 20+ & npm
- **Database**: PostgreSQL 16+

### Environment Setup

1. **Clone the repository**:
   ```bash
   git clone https://github.com/tanishqkale91-cmd/threadhub.git
   cd threadhub
   ```

2. **Configure Backend Environment**:
   Set environment variables or create a `.env` file in `./threadhub`:
   ```env
   DB_URL=jdbc:postgresql://localhost:5432/threadhub
   DB_USERNAME=threadhub_user
   DB_PASSWORD=threadhub_dev
   JWT_SECRET=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
   ```

3. **Configure Frontend Environment**:
   Create a `.env` file in `./frontend`:
   ```env
   VITE_API_BASE_URL=http://localhost:8080
   ```

---

## 3. Development Workflow

### Running the Backend
```bash
cd threadhub
./mvnw spring-boot:run
```

### Running Backend Tests
```bash
cd threadhub
DB_URL=jdbc:postgresql://localhost:5432/threadhub \
DB_USERNAME=threadhub_user \
DB_PASSWORD=threadhub_dev \
JWT_SECRET=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970 \
./mvnw test
```

### Running the Frontend
```bash
cd frontend
npm install
npm run dev
```

### Running Frontend Checks
```bash
cd frontend
npm run lint   # Runs Oxlint
npm run build  # Builds production bundle
```

---

## 4. Coding & Architectural Standards

### Backend Conventions
1. **Thin Controllers, Thick Services**: Keep REST controllers thin. Execute business logic inside `@Service` classes annotated with `@Transactional`.
2. **DTO Request Validation**: Use Bean Validation annotations (`@Valid @RequestBody`, `@NotBlank`, `@Size`, `@Email`) on **DTO classes**, never on JPA entities directly.
3. **Security First**:
   - Never accept client-supplied user IDs for operations on behalf of the authenticated user.
   - Extract user identity safely using `SecurityUtils.getCurrentUserId()`.
   - Never print or expose secrets, passwords, or tokens in server logs or response payloads.
4. **Standardized Error Handling**:
   - Register custom domain exceptions in `GlobalExceptionHandler`.
   - Ensure all API error responses use the standard `ErrorResponse` schema (`timestamp`, `status`, `error`, `message`, `details`).

### Frontend Conventions
1. **Component Architecture**: Organize components under `./src/components` (`common`, `layout`, `auth`) and pages under `./src/pages`.
2. **State & API**: Use `AuthContext` for session management and standard API clients in `./src/api`.
3. **Styling**: Use Tailwind CSS utilities cleanly without inline style overrides.

---

## 5. Submitting a Pull Request (PR)

1. **Create a Feature Branch**:
   ```bash
   git checkout -b feature/issue-1-post-crud
   ```

2. **Commit Message Format**:
   Use Conventional Commits syntax:
   - `feat: add post creation endpoint`
   - `fix: resolve authorization bug on community leave`
   - `test: add unit tests for post service`
   - `docs: update API documentation in README`

3. **Pre-PR Verification Checklist**:
   - [ ] All backend unit and integration tests pass (`./mvnw test`).
   - [ ] Frontend builds cleanly without errors (`npm run build`).
   - [ ] Linter checks pass (`npm run lint`).
   - [ ] No secrets, hardcoded credentials, or `.env` files are committed.
   - [ ] README/API documentation is updated if new endpoints were introduced.

4. **Submit PR**: Push your branch to GitHub and create a Pull Request targeting the `main` branch. GitHub Actions CI will automatically run verification checks.
