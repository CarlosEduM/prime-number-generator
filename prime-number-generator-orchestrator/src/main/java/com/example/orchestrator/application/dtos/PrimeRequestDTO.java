package com.example.orchestrator.application.dtos;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PrimeRequestDTO(
    UUID id,
    BigInteger inputNumberMin,
    BigInteger inputNumberMax,
    String status,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    List<ProcessingResultDTO> result
) {
}
