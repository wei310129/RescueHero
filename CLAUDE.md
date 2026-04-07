# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**RescueHero** (救援任務後勤管理平台) is a rescue mission logistics platform using a frontend-backend separation architecture with distributed, event-driven design. The backend runs 3 instances behind Nginx for high availability.

## Build & Run Commands

### Backend (Java 21 / Spring Boot)

```bash
# Build (skip tests)
./mvnw clean package -DskipTests -B

# Build with tests
./mvnw clean package -B

# Run locally
./mvnw spring-boot:run

# Apply code formatting (required before commits)
./mvnw spotless:apply

# Encrypt a config value with Jasypt
./mvnw jasypt:encrypt-value -Djasypt.encryptor.password=<password> -Djasypt.plugin.value=<plaintext>
```

**Required VM options for local development:**
```
--enable-preview
-Djasypt.encryptor.password=AAALKJPO872349JJMjskkKLfs0ekvZZZ
-DJWT_SECRET=AAALKJPO872349JJMjskkKLfs0ekvZZZ
-Dserver.port=8081
-Dchat.kafka.group-id.chat=rescuehero-chat-1
```

### Frontend (Vue.js 3)

```bash
cd frontend
npm install
npm run serve    # Dev server with hot reload
npm run build    # Production build
npm run lint     # Lint & fix
```

### Docker (Full Stack)

```bash
docker-compose -f docker/compose.yaml up -d
docker-compose -f docker/compose.yaml down
```

**Service ports:**
- Nginx (HTTPS): 443 / (HTTP): 80
- Frontend: 8080
- Backend instances: 8081, 8082, 8083
- Swagger UI: `http://localhost:8081/api/swagger-ui.html`
- PostgreSQL: 5432, MongoDB: 27017, Redis: 6379

## Architecture

### Backend

Spring Boot 3.5.6 on Java 21 (preview features enabled for virtual threads). Context path is `/api`.

**Multi-database strategy:**
- **PostgreSQL 16** — primary relational store; JPA with `ddl-auto: none` (manual schema management)
- **MongoDB 6.0** — document/unstructured data and optional session storage
- **Redis 7.2** — caching (Spring Cache) and session management

**Key stack:** Spring Security + JWT (stateless), QueryDSL for type-safe dynamic queries, MapStruct for DTO mapping, Jasypt for encrypted config values, Resilience4j circuit breaker, SpringDoc OpenAPI.

**Real-time messaging flow:**
1. Client sends message via WebSocket/STOMP → Spring app
2. App publishes to Kafka topic (`rescuehero.chat.group` or `rescuehero.chat.team`)
3. `ChatKafkaListener` consumes and broadcasts to all connected WebSocket clients

**Kafka cluster:** 5 brokers in KRaft mode (no Zookeeper), replication factor 3, min ISR 2, manual offset commits, 3 listener threads per consumer. Each backend instance uses a unique consumer group-id so all instances receive all messages.

**Domain structure (DDD):** Code is organized under `src/main/java/tw/com/aidenmade/rescuehero/domain/` into 10 domains: `account`, `address`, `base`, `chat`, `disaster`, `household`, `notification`, `rescue`, `resource`, `storage`. Each domain follows:
```
domain/[name]/
├── api/controller/       # REST endpoints
├── api/request|response/ # DTOs for HTTP layer
├── application/service/  # Business logic
├── entity/               # JPA entities
├── repository/           # Spring Data JPA
├── mapstruct/            # Entity ↔ DTO mappers
└── projection/           # DB projections
```

**Configuration classes** are in `configuration/config/` — one class per concern (Security, Kafka, Redis, QueryDsl, WebSocket, Swagger, DataSource, JPA Auditing).

### Frontend

Vue.js 3 SPA. Key utilities:
- `src/utils/apiFetch.js` — centralized API client; attaches JWT `Authorization` header and handles 401 errors
- `src/utils/logout.js` — clears token, closes WebSocket, redirects to login
- `src/components/ChatWidget.vue` — real-time chat using SockJS + STOMP

### Infrastructure

Nginx handles SSL termination and reverse proxies to the 3 backend instances. Backend instances are stateless (JWT auth, Redis sessions) so no sticky sessions are needed. Health checks hit `/api/actuator/health`.

Docker Secrets (`docker/secrets/`) manage sensitive credentials in production. Development secrets are in `docker/.env`.

## Important Notes

- **Java preview features** (`--enable-preview`) are required at both compile time and runtime — already configured in `pom.xml` via `maven-compiler-plugin` and `maven-surefire-plugin`.
- **Jasypt-encrypted values** in `application.yaml` use `ENC(...)` syntax. The encryption password must be provided at runtime via `-Djasypt.encryptor.password`.
- **JPA DDL is disabled** (`ddl-auto: none`) — schema changes require manual SQL migration scripts in `docker/postgres/init/`.
- **Spotless** enforces code formatting — run `./mvnw spotless:apply` before committing backend changes.
