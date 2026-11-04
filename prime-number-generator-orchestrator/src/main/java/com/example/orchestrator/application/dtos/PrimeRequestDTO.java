package com.example.orchestrator.application.dtos;

import java.math.BigInteger;

import jakarta.validation.constraints.NotNull;

public record PrimeRequestDTO(
        @NotNull BigInteger inputNumberMin,
        @NotNull BigInteger inputNumberMax) {
}
