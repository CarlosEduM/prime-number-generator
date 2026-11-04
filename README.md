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
with the request ID, range, status, and timestamps. The owner is taken from the
validated JWT `sub` claim, never from the request body.

Configure `OAUTH2_ISSUER_URI` with the issuer URI of the trusted authorization
server. Spring Security validates bearer tokens against that issuer; requests
without a valid token receive `401 Unauthorized`.

## Setup

To set up the project locally, follow these steps:

1. **Clone the Repository**: