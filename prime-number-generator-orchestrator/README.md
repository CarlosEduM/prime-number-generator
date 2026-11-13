# Prime Number Generator Orchestrator

The orchestrator is the HTTP entry point and workflow coordinator for the
prime-number generation system. It accepts authenticated requests, stores each
request and its processing record in PostgreSQL, publishes work to Kafka, and
updates the stored state when the processor reports progress or a result.

## Why this service is event-driven

This microservice was created to orchestrate prime-number generation through a
scalable, event-driven architecture. A request is handed off to a Kafka topic
instead of making the HTTP client wait for the calculation to finish. This
allows the API to accept new requests while earlier calculations are still
running, and keeps request handling separate from the processor's compute work.

The orchestrator owns request tracking and coordination; it does not calculate
prime numbers itself. The processor is a separate service that consumes work
and publishes result updates. Kafka decouples the two services in time and
allows each to scale independently, provided the broker, database, and
processor are available and correctly configured.

## Workflow

1. A client submits a range to `POST /api/prime-requests` with a valid bearer
   token.
2. The orchestrator uses the JWT `sub` claim as the request owner, validates
   the range, and persists the request with `PENDING` status and an associated
   processing result in `IN_QUEUE` state.
3. It publishes the range to the configured request topic. The Kafka message
   key is the processing result UUID; the JSON body contains the minimum and
   maximum values.
4. The API returns the new request UUID. The HTTP call does not wait for prime
   generation to complete, although publishing waits for Kafka's send result.
5. The processor reports `PROCESSING`, `SUCCESS`, or `FAIL` updates to the
   configured result topic, using the same processing result UUID as the
   message key.
6. The orchestrator finds the associated request by that key and persists the
   processing result and request status update.
7. The client can retrieve the request or list its requests through the
   authenticated API.

### Request status vs. processing state

The application deliberately uses `status` and `state` as separate concepts:

- The request `status` is a general description of the request lifecycle. It
  moves from `PENDING` to `PROCESSING` and then to `COMPLETED`. There is no
  failure status because `COMPLETED` means the request workflow has finished,
  not that prime generation necessarily succeeded.
- The processing result `state` describes the prime-generation operation
  specifically. It moves from `IN_QUEUE` to `PROCESSING` and ends in either
  `SUCCESS` when the primes were generated or `FAIL` when an error occurred.

This distinction makes the general lifecycle of a request easy to tell apart
from the outcome of the internal calculation. A request can therefore have
status `COMPLETED` while its processing result has state `SUCCESS` or `FAIL`.

## API

All API routes require a valid JWT bearer token. The owner is derived from the
validated token's `sub` claim and cannot be supplied or overridden in the
request body. Requests and lists are scoped to that owner.

### Create a request

```http
POST /api/prime-requests
Authorization: Bearer <JWT>
Content-Type: application/json

{
  "inputNumberMin": 0,
  "inputNumberMax": 100
}
```

Both values are required integers (JSON numbers). The minimum must be
non-negative, and the difference between the maximum and minimum must be
between 10 and 1,000,000 inclusive.

On success, the response is `201 Created` with the new request UUID as a JSON
string. Invalid input returns `400 Bad Request`; requests without a valid token
return `401 Unauthorized`.

### Get one request

```http
GET /api/prime-requests/{id}
Authorization: Bearer <JWT>
```

Returns `200 OK` with the request and its associated processing result. A
request that does not exist or belongs to another user returns `404 Not Found`.

### List requests

```http
GET /api/prime-requests?page=0&size=20
Authorization: Bearer <JWT>
```

The list contains only the authenticated user's requests. `page` is zero-based
and defaults to `0`; `size` defaults to `20` and must be at least `1`. The
response includes `content`, `page`, `size`, and `totalElements`. Invalid
pagination values return `400 Bad Request`.

OpenAPI documentation is available at `/swagger-ui/index.html`; the Swagger UI
and `/v3/api-docs/**` routes do not require authentication.

## Kafka contracts

Topic names and broker settings are configurable (see
[Configuration](#configuration)). Configure the processor with matching topic
names and the same processing-result UUID as the message key for each update.

### Request topic

The orchestrator publishes a JSON message to `prime-number-requests` by
default. Its key is the processing result UUID and its body has this shape:

```json
{
  "inputNumberMin": 0,
  "inputNumberMax": 100
}
```

The processor uses the range to do the calculation and publishes progress and
final updates to the result topic.

### Result topic

The orchestrator consumes `ProcessingResultUpdateDTO` JSON messages from
`prime-number-results` by default. The Kafka key must be the processing result
UUID. The body has `state`, `startedAt`, `results`, `errorMessage`, and
`finishedAt` fields. Fields that do not apply to a state must be `null`.
Timestamps use the local date-time format shown below.

`PROCESSING` requires `startedAt`:

```json
{
  "state": "PROCESSING",
  "startedAt": "2026-11-11T16:00:00",
  "results": null,
  "errorMessage": null,
  "finishedAt": null
}
```

`SUCCESS` requires a `results` array and `finishedAt`:

```json
{
  "state": "SUCCESS",
  "startedAt": null,
  "results": [2, 3, 5, 7, 11],
  "errorMessage": null,
  "finishedAt": "2026-11-11T16:00:02"
}
```

`FAIL` requires an `errorMessage` and `finishedAt`:

```json
{
  "state": "FAIL",
  "startedAt": null,
  "results": null,
  "errorMessage": "Failed to generate prime numbers for the requested range.",
  "finishedAt": "2026-11-11T16:00:02"
}
```

An `IN_QUEUE` update is not accepted. The processing result must follow the
`IN_QUEUE -> PROCESSING -> SUCCESS|FAIL` lifecycle.

## Dependencies

### Runtime services

- **Java 25** to run the application.
- **PostgreSQL** to persist prime requests and processing results.
- **Apache Kafka** to publish ranges and consume processing updates.
- **A trusted JWT issuer** that Spring Security can use to validate bearer
  tokens.
- **The processor service** to consume range messages and publish result
  updates.

Docker Compose can provide PostgreSQL and Kafka for local development. The
orchestrator module's Maven dependencies include Spring Boot web MVC, bean
validation, Spring Data JPA, Spring Kafka, the OAuth2 resource server, the
PostgreSQL JDBC driver, and Springdoc OpenAPI. H2 and Spring Boot test
dependencies are used for tests.

### Build and test tools

- **Maven** (the repository includes Maven Wrapper scripts).
- **Docker with the Compose plugin** for the full local stack.

## Run locally

The recommended way to start the local dependencies and the orchestrator is
from the repository root:

```bash
docker compose up --build
```

When the containers are ready, the API is at `http://localhost:8080`, and the
Kafka UI is at `http://localhost:8081`. The Compose setup configures PostgreSQL
and Kafka as dependencies of the orchestrator. PostgreSQL is available to
local database tools at `localhost:5432`; its data is kept in the
`postgres_data` volume. Kafka is available at `localhost:29092` from the host
and `kafka:9092` from other Compose containers.

To stop the stack:

```bash
docker compose down
```

To build or run the orchestrator's tests independently:

```bash
cd prime-number-generator-orchestrator
./mvnw test
```

For a standalone run, start PostgreSQL and Kafka separately, provide the
required environment variables below, and run:

```bash
./mvnw spring-boot:run
```

## Configuration

The application reads configuration from environment variables. Topic names
and the Kafka consumer group have defaults; database credentials and the JWT
issuer must be supplied.

| Variable | Default | Purpose |
| --- | --- | --- |
| `DB_URL` | `jdbc:postgresql://localhost:5432/prime_number_generator` | PostgreSQL JDBC URL |
| `DB_USERNAME` | None | PostgreSQL username |
| `DB_PASSWORD` | None | PostgreSQL password |
| `OAUTH2_ISSUER_URI` | None | Trusted JWT issuer URI |
| `KAFKA_BOOTSTRAP_SERVERS` | `localhost:9092` | Kafka broker address |
| `KAFKA_REQUEST_TOPIC` | `prime-number-requests` | Topic for range requests |
| `KAFKA_RESULT_TOPIC` | `prime-number-results` | Topic for result updates |
| `KAFKA_PROCESSING_RESULT_GROUP_ID` | `prime-number-generator-orchestrator` | Consumer group for result updates |

The root Compose configuration uses `https://accounts.google.com` as its
development issuer. Issuer validation does not by itself restrict tokens to a
specific OAuth client audience. Configure and verify audience validation
before exposing the service in production. The default database credentials in
Compose are for local development only; set `DB_USERNAME` and `DB_PASSWORD`
before starting the stack in any shared or deployed environment.

## Persistence and implementation notes

The orchestrator stores requests and processing results as related JPA
entities in PostgreSQL. The request stores its owner, range, status, and
timestamps. Its associated processing result stores its own UUID, state,
timestamps, any returned prime list, and any error message. The application
currently uses Hibernate schema update (`spring.jpa.hibernate.ddl-auto=update`)
at startup.

The code is organized into four layers:

- `presentation`: REST controllers, response DTOs, and exception handling.
- `application`: use cases, request/result DTOs, and the messaging interface.
- `domain`: request/result entities, state rules, exceptions, and repository
  interfaces.
- `infrastructure`: Kafka producer/consumer, JPA adapters, and framework
  configuration.

The result-topic Kafka consumer is intentionally in `infrastructure`. It
receives an internal processing-state update and adapts it to a call to the
existing `ProcessingResultUpdateService`; it does not initiate a new
client-facing request or prime-generation workflow. This differs from the
processor's request consumer, which is the processor's sole inbound job entry
point and is placed in `presentation`. The placement reflects these services'
different responsibilities, rather than a universal rule for Kafka consumers.

The service persists the initial request before publishing its Kafka message;
database persistence and Kafka publication are not implemented as a single
atomic transaction. Plan operational recovery accordingly. The current design
also relies on valid, ordered processor updates: processing results enforce the
`IN_QUEUE -> PROCESSING -> SUCCESS|FAIL` transition.
