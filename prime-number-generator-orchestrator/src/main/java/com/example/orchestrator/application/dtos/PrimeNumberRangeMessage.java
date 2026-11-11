package com.example.orchestrator.application.dtos;

import java.math.BigInteger;

public record PrimeNumberRangeMessage(BigInteger inputNumberMin, BigInteger inputNumberMax) {
}
