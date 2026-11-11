# Prime Number Generator System

## Project Description

This project aims to create a microservice architecture for generating prime numbers asynchronously using queues. The system consists of an orchestrator service that emits events and persists performance metrics. The service layer proccess each request's owner and scopes reads to that owner; authentication and the REST presentation layer are not implemented yet. The processor service consume the events, process them, and return the results back to the orchestrator via events in a message broker.

### Key Features

- **Orchestrator Service**: Receives requests from users to generate prime numbers within a specified range. It stores request information in a PostgreSQL database, sends the request to a Kafka queue for processing, and handles result retrieval via REST API.
- **Processor Service**: Listens to the Kafka queue for prime number generation requests, processes them, and returns results back to the orchestrator.
- **Data Store**: Uses a PostgreSQL database to store information about prime number generation requests and their results.
- **External Integration**: Utilizes Apache Kafka for inter-service communication via message queues.

### Technologies

- **Java**: The primary programming language.
- **Spring Boot**: A Java-based open-source framework for creating stand-alone applications.
- **Maven**: A software project management and comprehension tool.
- **PostgreSQL**: A relational database management system.
- **Kafka**: Apache Kafka, a distributed streaming platform.

### Deployment

- **Docker**: Containerization technology to package and run applications.
- **Composer**: A tool for defining and running multi-container Docker applications.

## Requesting a Prime Number Range

The orchestrator exposes an authenticated endpoint to create a prime-number request:

```http
POST /api/prime-requests
Authorization: Bearer <JWT>
Content-Type: application/json

{
  "inputNumberMin": 0,
  "inputNumberMax": 100
}
```

The range values are integers. The minimum must be non-negative, and the maximum
must be 10–1000 greater than the minimum. A successful request returns `201 Created`
with only the new request ID in the response body. The owner is taken from the
validated JWT `sub` claim, never from the request body.

Retrieve a request belonging to the authenticated user with:

```http
GET /api/prime-requests/{id}
Authorization: Bearer <JWT>
```

The endpoint returns `200 OK` with the request details, or `404 Not Found` when
the request does not exist or belongs to another user.

List the authenticated user's requests with optional pagination parameters:

```http
GET /api/prime-requests?page=0&size=20
Authorization: Bearer <JWT>
```

`page` is zero-based and `size` must be at least 1. Both default to `0` and `20`,
respectively. The response includes the current page and total element count.

Configure `OAUTH2_ISSUER_URI` with the issuer URI of the trusted authorization
server. Spring Security validates bearer tokens against that issuer; requests
without a valid token receive `401 Unauthorized`.

## Running with Docker Compose

The root `compose.yaml` starts the orchestrator, PostgreSQL, and a local Kafka
broker. The orchestrator publishes requests to `prime-number-requests`, with
the processing result ID as the Kafka key and only the requested range in the
message body. It consumes processing updates from `prime-number-results`; the
message key must be that same processing result ID and the body must match
`ProcessingResultUpdateDTO`.

Example processing-result update payloads:

- `PROCESSING`: [processing.json](./examples/processing-result-updates/processing.json)
- `SUCCESS`: [success.json](./examples/processing-result-updates/success.json)
- `FAIL`: [fail.json](./examples/processing-result-updates/fail.json)

The processing result ID is sent as the Kafka message key, not in the JSON
body. Fields unused for a given state must be `null`.

The Kafka UI is available at `http://localhost:8081`.

The Kafka bootstrap address is `kafka:9092` from containers and
`localhost:29092` from the host. Topic names can be overridden with
`KAFKA_REQUEST_TOPIC` and `KAFKA_RESULT_TOPIC`; configure the corresponding
topic names for the processor as well. `KAFKA_BOOTSTRAP_SERVERS` can override
the broker address for an externally managed Kafka cluster.

From the
repository root, run:

```bash
docker compose up --build
```

The API is available at `http://localhost:8080`. PostgreSQL data is persisted
in the `postgres_data` volume and is not exposed on a host port. For local
development the compose file has default database credentials; override them
by setting `DB_USERNAME` and `DB_PASSWORD` in the environment before starting
the services.

The compose file configures `https://accounts.google.com` as the JWT issuer.
Issuer validation alone does not restrict tokens to a specific Google OAuth
client; configure audience validation against your Google client ID before
using this setup as a production resource server.

## Setup

To set up the project locally, follow these steps:

1. **Clone the Repository**: