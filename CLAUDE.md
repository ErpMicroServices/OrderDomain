# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Repository Overview

This is a Domain-Driven Design (DDD) microservices template repository called OrderDomain. It provides a foundation for building domain-driven microservices with Spring Boot GraphQL API and React frontend.

**Note**: The build files reference "PeopleAndOrganizationsDomain" - this appears to be a naming inconsistency that should be addressed when customizing for your specific domain.

## Technology Stack

- **Backend**: Java 21, Spring Boot 3.4.5, Spring GraphQL, Spring Security with OAuth2
- **Database**: PostgreSQL 15 with Spring Data JPA
- **Caching**: Redis 7
- **Frontend**: React 19.1.0 with Vite
- **Testing**: JUnit 5, Cucumber BDD, Testcontainers
- **Build Tool**: Gradle 8.x (multi-module)
- **Containerization**: Docker and Docker Compose

## Common Development Commands

### Backend Development (API Module)

```bash
# Run all tests (unit tests only, excluding integration)
./gradlew :api:test

# Run integration tests
./gradlew :api:integrationTest

# Run BDD tests (Cucumber)
./gradlew :api:bddTest

# Run all quality checks
./gradlew :api:check

# Run quality gate (all checks + coverage verification)
./gradlew :api:qualityGate

# Build the API
./gradlew :api:build

# Run specific test class
./gradlew :api:test --tests "com.example.YourTestClass"

# Run specific test method
./gradlew :api:test --tests "com.example.YourTestClass.yourTestMethod"
```

### Frontend Development (UI Components)

```bash
# Start development server
cd ui-components && npm run dev

# Build for production
cd ui-components && npm run build

# Run linting
cd ui-components && npm run lint

# Preview production build
cd ui-components && npm run preview
```

### Docker Development

```bash
# Start all services (PostgreSQL, Redis, API)
docker-compose up -d

# View logs
docker-compose logs -f api

# Stop all services
docker-compose down

# Stop and remove volumes (clean slate)
docker-compose down -v
```

## Architecture and Project Structure

### Multi-Module Gradle Project

```
OrderDomain/
├── api/                    # Spring Boot GraphQL API
│   ├── src/
│   │   ├── main/          # Application source code
│   │   └── test/          # Unit, integration, and BDD tests
│   └── build.gradle       # API-specific dependencies
├── database/              # Database module (minimal currently)
├── ui-components/         # React frontend with Vite
├── config/               # Code quality configuration
│   ├── checkstyle/       # Java code style rules
│   ├── pmd/             # Static analysis rules
│   ├── spotbugs/        # Bug detection exclusions
│   └── owasp/           # Security vulnerability suppressions
├── docker/              # Docker-related files
└── build.gradle         # Root build configuration
```

### Domain-Driven Design Structure

The API module follows DDD principles with these key patterns:
- **Domain Layer**: Core business logic and domain models
- **Application Layer**: Use cases and application services
- **Infrastructure Layer**: External integrations and persistence
- **API Layer**: GraphQL resolvers and DTOs

### Testing Strategy

1. **Unit Tests** (80% minimum coverage)
   - Run with `./gradlew :api:test`
   - Excludes integration tests via tag exclusion

2. **Integration Tests**
   - Run with `./gradlew :api:integrationTest`
   - Uses JUnit platform with 'integration' tag
   - Includes Testcontainers for database testing

3. **BDD Tests** (Cucumber)
   - Run with `./gradlew :api:bddTest`
   - Located in test resources with .feature files
   - Uses Given-When-Then format

### Security Configuration

- OAuth2 authentication (standalone, no AWS dependencies)
- Configurable via environment variables
- Security scanning with OWASP Dependency Check
- Static security analysis with SpotBugs + FindSecBugs

### Quality Gates

The project enforces these quality standards:
- **Code Coverage**: 80% minimum (JaCoCo)
- **Code Style**: Checkstyle with custom rules
- **Static Analysis**: PMD and SpotBugs
- **Security**: OWASP dependency check (currently disabled)
- **Test Pass Rate**: 100% required

### Environment Configuration

#### Local Development (docker-compose.yml)
- PostgreSQL: localhost:5432
- Redis: localhost:6379
- API: localhost:8080
- OAuth2 configuration via environment variables

#### Key Environment Variables
- `OAUTH2_CLIENT_ID`: OAuth2 client identifier
- `OAUTH2_CLIENT_SECRET`: OAuth2 client secret
- `OAUTH2_ISSUER_URI`: OAuth2 provider issuer URI
- `SPRING_PROFILES_ACTIVE`: Active Spring profile (development, production)

## Development Workflow

1. **Feature Development**
   - Create feature branch from main
   - Write integration tests first (BDD/Cucumber)
   - Write unit tests (TDD approach)
   - Implement feature code
   - Run quality gate before committing

2. **Testing Sequence**
   ```bash
   ./gradlew :api:test          # Unit tests
   ./gradlew :api:integrationTest # Integration tests  
   ./gradlew :api:bddTest       # BDD tests
   ./gradlew :api:qualityGate  # All checks
   ```

3. **Local Development with Docker**
   ```bash
   docker-compose up -d         # Start services
   ./gradlew :api:bootRun      # Run API locally
   cd ui-components && npm run dev # Run frontend
   ```

## Important Notes

- This is a template repository - customize CLAUDE.md for your specific domain
- The project name inconsistency (OrderDomain vs PeopleAndOrganizationsDomain) needs resolution
- OWASP dependency check is currently disabled (skip = true) in build.gradle
- No main README.md exists - consider creating one for project documentation
- Frontend tests are not yet configured (package.json shows placeholder)