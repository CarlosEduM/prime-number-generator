# Architecture Overview
This document serves as a critical, living template designed to equip agents with a rapid and comprehensive understanding of the codebase's architecture, enabling efficient navigation and effective contribution from day one. Update this document as the codebase evolves.

## 1. Project Structure
This section provides a high-level overview of the project's directory and file structure, categorised by architectural layer or major functional area. It is essential for quickly navigating the codebase, locating relevant files, and understanding the overall organization and separation of concerns.

[Project Root]/
├── prime-number-generator-orchestrator/  # Orchestrator service
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/example/orchestrator/
│   │   │   │       ├── domain/            # Domain layer (if any)
│   │   │   │       ├── application/       # Application layer
│   │   │   │       ├── infrastructure/  # Infrastructure layer
│   │   │   │       └── presentation/    # Presentation layer (controllers and DTOs)
│   │   │   └── resources/
│   │   │       └── application.yml    # Configuration files
│   │   └── test/
│   │       ├── java/
│   │       │   └── com/example/orchestrator/
│   │       │       ├── domain/            # Domain tests (if any)
│   │       │       ├── application/       # Application layer tests
│   │       │       ├── infrastructure/  # Infrastructure tests (if any)
│   │       │       └── presentation/    # Presentation layer tests
│   │       └── resources/
│   │           └── test-application.yml # Test configuration files
│   └── pom.xml
├── prime-number-generator-processor-java/     # Processor service  
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/example/processor/
│   │   │   │       ├── domain/            # Domain layer (if any)
│   │   │   │       ├── application/       # Application layer
│   │   │   │       ├── infrastructure/  # Infrastructure layer
│   │   │   │       └── presentation/    # Presentation layer (controllers and DTOs)
│   │   │   └── resources/
│   │   │       └── application.yml    # Configuration files
│   │   └── test/
│   │       ├── java/
│   │       │   └── com/example/processor/
│   │       │       ├── domain/            # Domain tests (if any)
│   │       │       ├── application/       # Application layer tests
│   │       │       ├── infrastructure/  # Infrastructure tests (if any)
│   │       │       └── presentation/    # Presentation layer tests
│   │       └── resources/
│   │           └── test-application.yml # Test configuration files
│   └── pom.xml
├── scripts/              # Automation scripts (e.g., deployment, data seeding)
├── compose.yaml          # Compose file
├── .gitignore            # Specifies intentionally untracked files to ignore
├── README.md             # Project overview and quick start guide
└── ARCHITECTURE.md       # This document

## 2. High-Level System Diagram
Provide a simple block diagram (e.g., a C4 Model Level 1: System Context diagram, or a basic component diagram) or a clear text-based description of the major components and their interactions. Focus on how data flows, services communicate, and key architectural boundaries.

[User] <--> [Orchestrator Service API] <--> [Kafka Queue] <--> [Processor Service] <--> [Orchestrator Service API]

## 3. Core Components
(List and briefly describe the main components of the system. For each, include its primary responsibility and key technologies used.)

### 3.1. Orchestrator Service

Name: Prime Number Generator Orchestrator

Description: This service receives requests from users to generate prime numbers, stores them in a database, sends them to Kafka queue for processing, and handles result retrieval via REST API. It also manages authentication with API tokens.

Technologies: Java, Spring Boot, Maven, PostgreSQL, Kafka

Deployment: Docker, Composer

### 3.2. Processor Service

Name: Prime Number Generator Processor

Description: This service listens to the Kafka queue for prime number generation requests, processes them, and returns results back to the orchestrator via REST API.

Technologies: Java, Spring Boot, Maven, Kafka

Deployment: Docker, Composer

## 4. Data Stores

### 4.1. PostgreSQL Database

Name: Primary Database

Type: PostgreSQL

Purpose: Stores information about prime number generation requests and their results.

Key Schemas/Collections: 
- prime_requests (id, user_id, input_number, status, created_at, updated_at)
- processing_results (id, processor_name, request_id, status, result, processing_start_at, started_at, finished_at)

## 5. External Integrations / APIs

### 5.1. Apache Kafka

Purpose: Inter-service communication via message queues
Integration Method: Kafka Producer/Consumer API

## 6. Deployment & Infrastructure

TODO

## 7. Security Considerations

Authentication: API Key-based authentication with role-based access control (RBAC)

Authorization: 
- Clients: Access orchestrator service via API token with 'client' role
- Processors: Access orchestrator service via API token with 'processor' role

Data Encryption: TLS in transit for all communications

Key Security Tools/Practices: JWT tokens, secure API key storage, role-based access control

## 8. Project Identification

Project Name: Prime Number Generator System

Repository URL: [Insert Repository URL]

Primary Contact/Team: [Insert Lead Developer/Team Name]

Date of Last Update: 2026-07-17

## 9. Glossary / Acronyms

[API]: Application Programming Interface

[Kafka]: Apache Kafka, a distributed streaming platform

[RBAC]: Role-Based Access Control

[REST]: Representational State Transfer

[Spring Boot]: Java-based open source framework for creating stand-alone applications

[Maven]: A software project management and comprehension tool
