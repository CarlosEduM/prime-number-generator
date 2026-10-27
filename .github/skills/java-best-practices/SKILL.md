---
name: java-best-practices
description: "Apply this repository's Java and Spring Boot conventions when implementing, reviewing, or refactoring the prime-number-generator orchestrator or processor. Use for domain rules, persistence, service design, pagination, asynchronous processing, and tests."
argument-hint: "Describe the change or code to review in this repository"
---

# Prime Number Generator Best Practices

Use this skill to keep changes aligned with the existing Prime Number Generator code. These guidelines reflect the current state of the repository; always verify the implementation before treating documentation or architectural intent as an implemented feature.

## When to Use

- Implementing or reviewing Java features in the orchestrator and processor modules.
- Changing entities, domain rules, application services, repositories, or pagination.
- Working on JPA persistence, asynchronous processing, or inter-service communication.
- Writing or updating automated tests for this project.

## Procedure

1. **Understand the current state before editing.** Read applicable instructions, the target class, its collaborators and nearby tests, as well as the module's `pom.xml`. Check `git status` and preserve pre-existing changes. Use `ARCHITECTURE.md` and `README.md` as context, not as proof that an integration has been implemented.

2. **Respect module boundaries and responsibilities.** The orchestrator contains application, domain, and infrastructure packages under `com.example.orchestrator`; keep business rules in the domain, coordination in application services, and technical data access in infrastructure. The processor is a separate Spring Boot module; do not couple its internal details to the orchestrator. Before introducing a new layer or abstraction, look for an existing pattern and establish why it is needed.

3. **Keep domain rules explicit.** Domain entities should protect invariants through clearly intentioned operations and explicit errors, rather than exposing arbitrary state changes. When changing ranges, statuses, or results, preserve and test existing rules, including input boundaries, valid transitions, and behavior after completion. Use `BigInteger` for range values and prime results to avoid precision loss from conversions.

4. **Preserve boundaries between application and persistence.** Services receive dependencies through constructors and use domain repository interfaces; keep Spring Data/JPA code in infrastructure. Do not move business rules into queries or adapters. When changing JPA entities, check mappings, relationships, enums, constraints, and compatibility with tests and the configured database.

5. **Handle asynchronous contracts and states carefully.** When changing messages, processing, or results, verify the existing contracts in both modules and preserve coherent states and transitions. Do not assume that Kafka, endpoints, authentication, or infrastructure described in the documentation have been implemented: locate and follow the actual code before adding dependencies or behavior.

6. **Follow existing types and value objects.** Prefer immutable DTOs when the local pattern is `record`; use `PageableRequest` and `PageableResult` for pagination where applicable. Validate input at the appropriate boundary and do not introduce defaults or silent failures that hide invalid input.

7. **Write focused, deterministic tests.** Use JUnit 5 and Mockito, following existing tests. Construct the unit under test explicitly; isolate databases, brokers, networks, and Spring contexts from unit tests. Cover observable outcomes, boundaries, and failures that are part of the contract. Verify mock interactions only when they represent required behavior, not merely to mirror the implementation.

8. **Validate at the smallest useful scope.** First run tests for the affected module with the Maven Wrapper (`./mvnw test` from the respective module); broaden validation when a change affects more components. Clearly report which tests were run and any environmental limitations.

## Review Checklist

- Does the change preserve separation between domain, application, and infrastructure?
- Are rules and state transitions still explicit and covered by tests?
- Do numeric types avoid precision loss?
- Have inter-module contracts been verified without assuming planned features exist?
- Do tests cover relevant behavior without relying on external services?
- Were tests for the affected module run?
