package com.example.orchestrator.application.dtos;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

public record ProcessingResultResponseDTO(
        String processorName,
        String state,
        List<BigInteger> result,
        LocalDateTime startedAt,
        LocalDateTime finishedAt) {
}
