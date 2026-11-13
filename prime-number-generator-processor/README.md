# Prime Number Generator Processor

The processor is an internal worker in the prime-number generation system. Its
responsibility is deliberately narrow: receive a generation job, calculate
the primes in the requested range, and report progress and the final outcome
to the orchestrator.

The processor is isolated from the client's HTTP request. The orchestrator
accepts and records requests, then hands work to Kafka; the processor works
independently and sends updates back through Kafka. This separation is intended
to let the API keep accepting requests while long-running calculations are in
progress.

## Why isolate the processor

This service simulates a slow, compute-intensive process. Its current
brute-force generator checks candidate divisors up to the square root of each
candidate number. A workload taking around 30 minutes is a design scenario,
not a guaranteed runtime or a benchmark; actual duration depends on the range
and available resources.

Because calculation happens outside the HTTP request lifecycle, a slow job
does not occupy the client's connection or prevent the orchestrator from
accepting other requests. The processor can be given more CPU and memory
(vertical scaling) independently from the API. Worker instances can also be
scaled horizontally. Kafka queue depth can be used as an input to an
autoscaling policy, but this requires deployment-level autoscaling
configuration and sufficient Kafka topic partitions; it is not configured in
this repository today.

## Processing workflow

1. The orchestrator saves the request and publishes a range message to the
   Kafka request topic.
2. The processor consumes the message and uses the Kafka key as the processing
   result ID that correlates all updates for this job.
3. It publishes a `PROCESSING` update with a start timestamp.
4. It calculates the prime numbers in the range using the brute-force
   generator.
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

## Why the request consumer is in the presentation layer

The `PrimeNumberRequestKafkaConsumer` is in the processor's `presentation`
layer because consuming a range message is the service's only inbound path for
generation jobs. It adapts the Kafka key and payload into a call to the
application service. The consumer handles message transport; generation
behavior remains in the application and domain code.

The orchestrator uses a different placement for its result-topic consumer. It
keeps that consumer in `infrastructure` because the message is an internal
processing-state update that is adapted to an existing update service; it does
not start a new client request or generation workflow. This is a deliberate,
project-specific choice based on each consumer's role, not a general rule that
Kafka consumers always belong in one layer.

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
- **Spring for Apache Kafka** for message consumption and result publication.
- **Apache Kafka** as a runtime service.

The current Maven dependencies include the Spring Kafka starter, optional
Lombok, and the Spring Kafka test starter. The processor does not depend on
PostgreSQL or expose a client-facing HTTP API.

## Configuration

The processor must use the same request and result topics as the orchestrator.
These settings can be supplied through environment variables:

| Variable | Default | Purpose |
| --- | --- | --- |
| `KAFKA_BOOTSTRAP_SERVERS` | `localhost:9092` | Kafka broker address |
| `KAFKA_PROCESSING_REQUEST_GROUP_ID` | `prime-number-generator-processor` | Consumer group for generation jobs |
| `KAFKA_REQUEST_TOPIC` | `prime-number-requests` | Topic for generation jobs |
| `KAFKA_RESULT_TOPIC` | `prime-number-results` | Topic for progress and result updates |

## Build and test

From the repository root:

```bash
cd prime-number-generator-processor
./mvnw test
```

The module can also be started with Kafka available at the configured broker:

```bash
./mvnw spring-boot:run
```

When using the root Compose broker from the host, set
`KAFKA_BOOTSTRAP_SERVERS=localhost:29092`. The orchestrator should be configured
with the same topic names to persist the processor's updates.

To build and run the processor with the full local stack, start Compose from
the repository root:

```bash
docker compose up --build
```

Compose builds the processor image from this module's Dockerfile and starts the
worker after Kafka is healthy. The worker uses `kafka:9092` and the same
request and result topics as the orchestrator.

## Current implementation

The module currently includes:

- a Kafka request consumer in the `presentation` layer;
- a brute-force prime generator and an application service that reports
  `PROCESSING`, `SUCCESS`, or `FAIL`;
- a Kafka result producer in the `infrastructure` layer and configurable
  broker, topic, and consumer-group settings.

Queue-depth-based autoscaling is not configured. The orchestrator and shared
message formats are documented in the
[orchestrator README](../prime-number-generator-orchestrator/README.md).
