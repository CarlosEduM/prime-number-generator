package com.example.orchestrator.application.dtos;

import java.math.BigInteger;

public record PrimeRequestDTO(
        BigInteger inputNumberMin,
        BigInteger inputNumberMax) {
}
