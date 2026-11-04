---
name: layered-architecture
description: "Design, implement, review, or refactor software using layered architecture. Use when defining layer responsibilities, package boundaries, dependency direction, or deciding where presentation, application, domain, and infrastructure logic belongs."
argument-hint: "Describe the architecture question or code change"
---

# Layered Architecture

Use this skill when designing, implementing, reviewing, or refactoring software organized into architectural layers. First inspect the repository's existing structure and conventions; apply these principles without imposing layers or abstractions that the project does not need.

## Layer Responsibilities

### Presentation Layer

- Owns the system's inbound interfaces, such as REST controllers, CLI commands, UI handlers, or message-consumer entry points.
- Translates transport-specific input into application requests and application outcomes into transport-specific responses.
- Performs boundary-level validation, such as required fields, syntax, and request-shape checks.
- Handles protocol concerns such as HTTP status codes, headers, serialization, and transport-level error mapping.
- Must not implement business policies, access persistence directly, or expose infrastructure models as public contracts.

### Application Layer

- Orchestrates use cases and coordinates domain objects and required ports or services.
- Defines the steps and transaction boundaries needed to fulfill a use case.
- Enforces application workflow rules, such as authorization checks, sequencing, and interaction between domain operations.
- Converts between presentation-facing inputs/outputs and domain concepts when appropriate.
- Must not own core business invariants or depend directly on presentation frameworks or infrastructure implementations.

### Domain Layer

- Owns the core business concepts, rules, invariants, and state transitions.
- Models entities, value objects, domain services, and domain-level errors where they clarify the business.
- Protects valid state and makes business behavior explicit and testable.
- Should remain independent of UI, transport, databases, messaging systems, and framework-specific concerns.
- Must not contain HTTP handling, ORM mappings, serialization details, or application workflow coordination.

### Infrastructure Layer

- Implements technical capabilities required by the application or domain, such as persistence, messaging, external API clients, file storage, and framework configuration.
- Adapts infrastructure-specific APIs and data models to the application's or domain's abstractions.
- Contains repository adapters, ORM mappings, framework wiring, and integration configuration.
- Must not become the source of business policy; keep business decisions in the domain or application layer.
- Should be replaceable without requiring changes to core business rules.

## Dependency and Data Flow

- Keep dependencies directed inward: presentation may call application, and application may use domain abstractions.
- Infrastructure may depend on application or domain contracts to implement technical adapters. Core business code must not depend on infrastructure implementations.
- Typical inbound flow: Presentation -> Application -> Domain, with Application using ports implemented by Infrastructure when external capabilities are needed.
- Typical outbound flow: Infrastructure translates a persistence or external-system result into a model usable by Application or Domain.
- Avoid bypassing a layer, such as a controller calling a repository directly, unless the codebase has a deliberate and documented exception.
- Keep each layer's models and errors within their intended boundary; translate them at the boundary rather than leaking framework types inward.

## Procedure

1. Inspect the repository's architecture documentation, package structure, representative classes, and tests. Treat documentation as intent and verify the implementation.
2. Trace the relevant use case from its entry point through orchestration, business rules, and technical integrations.
3. Assign each behavior to the layer whose responsibility it serves, using the definitions above and existing project conventions.
4. Make the smallest change that preserves the boundaries. Introduce a port, adapter, or additional abstraction only when it clarifies a real dependency or seam.
5. Add or update tests at the layer that owns the behavior: domain tests for business rules, application tests for use-case coordination, presentation tests for input/output contracts, and infrastructure tests for technical mappings or integrations.
6. Run the smallest relevant test or validation command and report any unverified behavior or environmental limitation.

## Review Checklist

- Are business invariants owned and protected by the domain?
- Does the application layer coordinate use cases without duplicating domain policy?
- Does the presentation layer translate protocols without owning business or persistence logic?
- Are technical integrations and framework details isolated in infrastructure?
- Do dependencies point inward, with core rules independent of frameworks and external systems?
- Are layer boundaries and observable behavior covered by focused tests?
