package com.example.orchestrator.presentation.dtos;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.UUID;

import com.example.orchestrator.domain.entities.PrimeRequestStatus;
import com.example.orchestrator.domain.entities.PrimeRequests;

public record PrimeRequestResponseDTO(
        UUID id,
        BigInteger inputNumberMin,
        BigInteger inputNumberMax,
        PrimeRequestStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {

    public static PrimeRequestResponseDTO from(PrimeRequests request) {
        return new PrimeRequestResponseDTO(
                request.getId(),
                request.getInputNumberMin(),
                request.getInputNumberMax(),
                request.getStatus(),
                request.getCreatedAt(),
                request.getUpdatedAt());
    }
}
