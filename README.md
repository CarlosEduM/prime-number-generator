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

## Setup

To set up the project locally, follow these steps:

1. **Clone the Repository**: