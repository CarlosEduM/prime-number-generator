# Prime Number Generator Processor

The processor is intended to be an internal worker in the prime-number
generation system. Its responsibility is deliberately narrow: receive a
generation job, calculate the primes in the requested range, and report
progress and the final outcome to the orchestrator.

The processor is isolated from the client's HTTP request. The orchestrator
accepts and records requests, then hands work to Kafka; the processor works
independently and sends updates back through Kafka. This separation is intended
to let the API keep accepting requests while long-running calculations are in
progress.

## Why isolate the processor

This service is designed to simulate a slow, compute-intensive process. A
brute-force prime-generation algorithm is suitable for that purpose because
the calculation can be made deliberately expensive. A workload taking around
30 minutes is a design scenario, not a guaranteed runtime or a benchmark of
the current code.

Because calculation happens outside the HTTP request lifecycle, a slow job
does not occupy the client's connection or prevent the orchestrator from
accepting other requests. The processor can be given more CPU and memory
(vertical scaling) independently from the API. Worker instances can also be
scaled horizontally. Kafka queue depth can be used as an input to an
autoscaling policy, but this requires deployment-level autoscaling
configuration and sufficient Kafka topic partitions; it is not configured in
this repository today.

## Intended workflow

1. The orchestrator saves the request and publishes a range message to the
   Kafka request topic.
2. The processor consumes the message and uses the Kafka key as the processing
   result ID that correlates all updates for this job.
3. It publishes a `PROCESSING` update with a start timestamp.
4. It calculates the prime numbers in the range.
5. It publishes either a `SUCCESS` update containing the primes or a `FAIL`
   update containing an error message. Both final updates include a finish
   timestamp.
6. The orchestrator consumes these updates and persists the current processing
   state so the client can retrieve it through the orchestrator API.

The processor does not own the public API or request records. In the system's
model, the orchestrator's request `status` describes the general request
lifecycle, while the processing result `state` describes the calculation
(`IN_QUEUE`, `PROCESSING`, `SUCCESS`, or `FAIL`). A completed request can
therefore have a successful or failed processing state.

## Kafka integration contract

The processor and orchestrator must use matching Kafka topics. The default
topic names configured by the orchestrator are `prime-number-requests` and
`prime-number-results`.

### Input: prime-number-requests

The orchestrator publishes a JSON range message. The Kafka key is the
processing result UUID; it is not included in the JSON body.

```json
{
  "inputNumberMin": 0,
  "inputNumberMax": 100
}
```

### Output: prime-number-results

Every result update must use the same processing result UUID as its Kafka key.
The JSON body has `state`, `startedAt`, `results`, `errorMessage`, and
`finishedAt` fields. Fields not applicable to the selected state must be
`null`. Timestamps use local ISO date-time values, for example
`2026-11-11T16:00:00`.

- `PROCESSING`: requires `startedAt`; the other fields are `null`.
- `SUCCESS`: requires `results` and `finishedAt`; the other fields are `null`.
- `FAIL`: requires `errorMessage` and `finishedAt`; the other fields are
  `null`.

The orchestrator creates the processing result in `IN_QUEUE` before
dispatching the job. The processor should begin reporting with `PROCESSING`;
`IN_QUEUE` is not a valid update to the orchestrator. Example payloads are
available for [PROCESSING](../examples/processing-result-updates/processing.json),
[SUCCESS](../examples/processing-result-updates/success.json), and
[FAIL](../examples/processing-result-updates/fail.json).

## Dependencies

The processor module currently depends on:

- **Java 25** to build and run the application.
- **Maven** through the included Maven Wrapper.
- **Spring Boot** as the application framework.
- **Spring for Apache Kafka** for the intended message-consumer and
  message-producer integration.
- **Apache Kafka** as a runtime dependency once message processing is
  implemented.

The current Maven dependencies include the Spring Kafka starter, optional
Lombok, and the Spring Kafka test starter. The processor does not currently
depend on PostgreSQL or expose an HTTP API.

## Build and test

From the repository root:

```bash
cd prime-number-generator-processor
./mvnw test
```

The module can also be started with:

```bash
./mvnw spring-boot:run
```

At present, this only starts the Spring Boot application shell; it does not
consume Kafka messages or calculate primes.

## Current implementation status

This module is currently a Spring Boot bootstrap skeleton. It contains the
application entry point and a context-load test, but it does not yet include:

- a Kafka request listener or result producer;
- the prime-generation algorithm or a long-running worker;
- Kafka broker, topic, or consumer-group configuration;
- progress reporting and failure handling;
- a processor Dockerfile or a processor service in the root Compose setup.

The workflow, contracts, and scaling discussion above describe the intended
design. They should be updated as these capabilities are implemented. The
orchestrator and its message formats are documented in the
[orchestrator README](../prime-number-generator-orchestrator/README.md).
