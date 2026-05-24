# AssetTrack API

**Industrial asset management system** — Real-time WebSocket alerts, JWT authentication, and Clean Architecture built with Java 21 + Spring Boot 3.5.

![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5-6DB33F?style=flat-square&logo=springboot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-4169E1?style=flat-square&logo=postgresql&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-7-DC382D?style=flat-square&logo=redis&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?style=flat-square&logo=docker&logoColor=white)
![JWT](https://img.shields.io/badge/Auth-JWT-000000?style=flat-square&logo=jsonwebtokens&logoColor=white)
![WebSocket](https://img.shields.io/badge/Realtime-WebSocket-010101?style=flat-square&logo=socket.io&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-22C55E?style=flat-square)
![Build](https://img.shields.io/badge/Build-Passing-22C55E?style=flat-square&logo=githubactions&logoColor=white)
![Architecture](https://img.shields.io/badge/Architecture-Clean-7F77DD?style=flat-square)

---

## Overview

AssetTrack is a backend REST API designed for industrial environments. It allows teams to manage physical equipment (assets), create and track maintenance work orders, receive sensor telemetry, and get notified instantly when a sensor reading exceeds a configured threshold.

Built as a portfolio project targeting industrial clients such as Petrobras, WEG, and Embraer — the same segment served by [EDGE](https://edgetecnologia.com.br), an Alagoan tech company.

---

## Architecture

This project follows **Clean Architecture** with strict dependency rules — inner layers never depend on outer ones.

```
┌─────────────────────────────────────────┐
│           Adapters (HTTP / WS)          │  Controllers, Exception Handler
├─────────────────────────────────────────┤
│         Application (Use Cases)         │  Business orchestration, DTOs
├─────────────────────────────────────────┤
│           Domain (Core Logic)           │  Entities, Rules, Repository interfaces
├─────────────────────────────────────────┤
│        Infrastructure (Details)         │  JPA, Redis, JWT, WebSocket impl
└─────────────────────────────────────────┘
```

**Golden rule:** dependencies always point inward. The domain layer has zero framework dependencies — it's testable in pure Java.

---

## Features

- **Asset Management** — Full CRUD for industrial equipment with maintenance lifecycle
- **Maintenance Workflow** — State machine: `OPERATIONAL → UNDER_MAINTENANCE → OPERATIONAL`
- **Work Orders (OS)** — Workflow with 4 states: `OPEN → IN_PROGRESS → COMPLETED → VALIDATED`
- **Sensor Telemetry** — Receives IoT sensor readings and auto-generates alerts when thresholds are exceeded
- **Real-time Alerts** — WebSocket (STOMP) broadcasts alerts instantly to connected dashboards
- **JWT Authentication** — Stateless auth with access token (15min) + refresh token (7 days)
- **Role-based Authorization** — `ADMIN`, `GESTOR`, `TECNICO` with method-level `@PreAuthorize`
- **Monitoring** — Spring Actuator + Prometheus metrics + Grafana dashboards
- **API Docs** — Swagger UI with JWT auth integrated

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.5 |
| Security | Spring Security + JWT (JJWT 0.12) |
| Persistence | Spring Data JPA + PostgreSQL 16 |
| Cache | Redis 7 |
| Real-time | WebSocket + STOMP |
| Documentation | SpringDoc OpenAPI 3 (Swagger) |
| Monitoring | Actuator + Prometheus + Grafana |
| Unit Tests | JUnit 5 + Mockito + AssertJ |
| Integration Tests | Testcontainers |
| CI/CD | GitHub Actions |
| Containers | Docker + Docker Compose |

---

## Getting Started

### Prerequisites

- Docker + Docker Compose
- JDK 21 (to run locally without Docker)
- Maven 3.9+

### Run with Docker (recommended)

```bash
# Clone the repository
git clone https://github.com/alessandrogama/assettrack-api.git
cd assettrack-api

# Start the full stack
docker compose up -d

# Follow the API logs
docker compose logs -f api
```

| Service | URL |
|---|---|
| API | http://localhost:8080 |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| Grafana | http://localhost:3000 (admin / admin123) |
| Prometheus | http://localhost:9090 |

### Run locally (dev mode)

```bash
# Start only the databases
docker compose up -d postgres redis

# Run the application
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

---

## Authentication

The API uses JWT Bearer tokens. Seed users are created automatically on startup in dev/docker profiles:

| Username | Password    | Role |
|---|-------------|---|
| admin | admin123456 | ROLE_ADMIN |
| gestor | gestor123456  | ROLE_GESTOR |
| tecnico | tec123456     | ROLE_TECNICO |

**Get a token:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "admin", "password": "admin123"}'
```

**Use the token:**
```bash
curl http://localhost:8080/api/assets \
  -H "Authorization: Bearer <your_token_here>"
```

---

## API Endpoints

### Assets
| Method | Endpoint | Description | Min Role |
|---|---|---|---|
| GET | /api/assets | List all assets | TECNICO |
| GET | /api/assets/{id} | Get asset by ID | TECNICO |
| POST | /api/assets | Create asset | GESTOR |
| PUT | /api/assets/{id} | Update asset | GESTOR |
| POST | /api/assets/{id}/maintenance/start | Start maintenance | TECNICO |
| POST | /api/assets/{id}/maintenance/complete | Complete maintenance | TECNICO |
| POST | /api/assets/{id}/deactivate | Deactivate permanently | ADMIN |

### Work Orders
| Method | Endpoint | Description | Min Role |
|---|---|---|---|
| POST | /api/orders | Open work order | TECNICO |
| GET | /api/orders/{id} | Get order by ID | TECNICO |
| GET | /api/orders/asset/{assetId} | Orders for an asset | TECNICO |
| PATCH | /api/orders/{id}/assign | Assign technician | GESTOR |
| PATCH | /api/orders/{id}/complete | Complete order | TECNICO |
| PATCH | /api/orders/{id}/validate | Validate order | GESTOR |
| PATCH | /api/orders/{id}/reject | Reject order | GESTOR |

### Telemetry & Alerts
| Method | Endpoint | Description | Auth |
|---|---|---|---|
| POST | /api/telemetry | Receive sensor reading | Public |
| GET | /api/alerts/unacknowledged | Pending alerts | Required |
| GET | /api/alerts/asset/{assetId} | Alerts for an asset | Required |
| PATCH | /api/alerts/{id}/acknowledge | Acknowledge alert | Required |

---

## Real-time Alerts via WebSocket

Connect to receive instant alert notifications:

```javascript
const socket = new SockJS('http://localhost:8080/ws');
const client = Stomp.over(socket);

client.connect({}, () => {
  // All alerts
  client.subscribe('/topic/alerts', (msg) => {
    console.log('Alert:', JSON.parse(msg.body));
  });

  // Alerts for a specific asset
  client.subscribe('/topic/alerts/<asset-uuid>', (msg) => {
    console.log('Asset alert:', JSON.parse(msg.body));
  });
});
```

---

## Alert Severity Calculation

When a sensor reading exceeds its threshold, severity is calculated by the percentage above it:

| Excess above threshold | Severity |
|---|---|
| 0 – 10% | LOW |
| 10 – 25% | MEDIUM |
| 25 – 50% | HIGH |
| > 50% | CRITICAL |

---

## Running Tests

```bash
# All tests (unit + integration)
./mvnw clean verify

# Unit tests only (fast, no Docker needed)
./mvnw test -Dtest="*Test" -DfailIfNoTests=false

# Integration tests (requires Docker)
./mvnw test -Dtest="*IntegrationTest"
```

### Test strategy

| Layer | Approach | Speed |
|---|---|---|
| Domain | Pure JUnit — no Spring, no mocks | Very fast |
| Application | Mockito — mocked repositories | Fast |
| Infrastructure | Testcontainers — real PostgreSQL | Slower, most reliable |

---

## Project Structure

```
src/
├── main/java/com/assettrack/
│   ├── domain/
│   │   ├── entity/        # Asset, ServiceOrder, Alert, MaintenanceRecord
│   │   ├── valueobject/   # AssetStatus, OrderStatus, AlertSeverity
│   │   ├── repository/    # Repository interfaces (contracts)
│   │   └── exception/     # DomainException
│   ├── application/
│   │   ├── usecase/       # One use case class per business action
│   │   └── dto/           # Request and Response DTOs (Java records)
│   ├── infrastructure/
│   │   ├── persistence/   # JPA entities, mappers, repository implementations
│   │   ├── security/      # JWT service, filter, UserDetails
│   │   ├── config/        # SecurityConfig, WebSocketConfig, RedisConfig
│   │   └── messaging/     # WebSocketAlertNotification
│   └── adapters/
│       └── http/          # Controllers, GlobalExceptionHandler
└── test/
    ├── domain/            # Business rule tests
    ├── application/       # Use case tests with Mockito
    └── infrastructure/    # Integration tests with Testcontainers
```

---

## Environment Variables

| Variable | Description | Default |
|---|---|---|
| JWT_SECRET | Base64-encoded secret key | Example key (dev only) |
| POSTGRES_USER | Database username | postgres |
| POSTGRES_PASSWORD | Database password | dev |
| SPRING_PROFILES_ACTIVE | Active profile | dev |

---

## Architecture Decision Records

Key technical decisions are :

— Clean Architecture as project structure
— Separation between domain entity and JPA entity
— JWT with HS256 for authentication
— WebSocket with STOMP for real-time alerts
— Java Records for DTOs
— Testcontainers for integration tests

---

## License

This project is licensed under the MIT License.