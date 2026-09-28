package com.example.orchestrator.domain.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigInteger;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.example.orchestrator.domain.exceptions.IllegalOperationException;
import com.example.orchestrator.domain.exceptions.InvalidInputException;

public class PrimeRequestTest {
    private PrimeRequest primeRequest;

    @BeforeEach
    public void setUp() {
        primeRequest = new PrimeRequest();
    }

    @Test
    public void testSetInputNumberRangeValidValues() {
        BigInteger inputNumberMin = BigInteger.valueOf(0);
        BigInteger inputNumberMax = BigInteger.valueOf(10);

        primeRequest.setInputNumberRange(inputNumberMin, inputNumberMax);

        assertEquals(inputNumberMin, primeRequest.getInputNumberMin());
        assertEquals(inputNumberMax, primeRequest.getInputNumberMax());
    }

    @Test
    public void testSetInputNumberRangeInputNumberMinTooSmall() {
        BigInteger inputNumberMin = BigInteger.valueOf(-1);
        BigInteger inputNumberMax = BigInteger.valueOf(10);

        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
            primeRequest.setInputNumberRange(inputNumberMin, inputNumberMax);
        });

        assertEquals("Input number minimum must be greater than or equal to 0.", exception.getMessage());
    }

    @Test
    public void testSetInputNumberRangeInputNumberMinTooSmall2() {
        BigInteger inputNumberMin = BigInteger.valueOf(5);
        BigInteger inputNumberMax = BigInteger.valueOf(10);

        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
            primeRequest.setInputNumberRange(inputNumberMin, inputNumberMax);
        });

        assertEquals("Input number maximum must be at least 10 numbers larger than the minimum.", exception.getMessage());
    }

    @Test
    public void testSetInputNumberRangeInputNumberMaxTooSmall() {
        BigInteger inputNumberMin = BigInteger.valueOf(5);
        BigInteger inputNumberMax = BigInteger.valueOf(4);

        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
            primeRequest.setInputNumberRange(inputNumberMin, inputNumberMax);
        });

        assertEquals("Input number maximum must be at least 10 numbers larger than the minimum.",
                exception.getMessage());
    }

    @Test
    public void testSetInputNumberRangeInputNumberMinTooLarge() {
        BigInteger inputNumberMin = BigInteger.valueOf(1005);
        BigInteger inputNumberMax = BigInteger.valueOf(2006);

        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
            primeRequest.setInputNumberRange(inputNumberMin, inputNumberMax);
        });

        assertEquals("Input number maximum could not be 1000 numbers larger than minimum.", exception.getMessage());
    }

    @Test
    public void testSetInputNumberRangeInputNumberAlreadySet() {
        BigInteger inputNumberMin = BigInteger.valueOf(0);
        BigInteger inputNumberMax = BigInteger.valueOf(50);
        primeRequest.setInputNumberRange(inputNumberMin, inputNumberMax);

        IllegalOperationException exception = assertThrows(IllegalOperationException.class, () -> {
            primeRequest.setInputNumberRange(inputNumberMin, inputNumberMax);
        });

        assertEquals("Input number range already set.", exception.getMessage());
    }

    @Test
    public void testSetStatusSuccess() {
        PrimeRequestStatus newStatus = PrimeRequestStatus.PROCESSING;

        primeRequest.setStatus(newStatus);

        assertEquals(newStatus, primeRequest.getStatus());
    }

    @ParameterizedTest
    @MethodSource("provideStatusValidPairs")
    public void testSetStatusValidInput(PrimeRequestStatus actualStatus, PrimeRequestStatus newStatus) {
        if (actualStatus != PrimeRequestStatus.PENDING)
            primeRequest.setStatus(actualStatus);

        primeRequest.setStatus(newStatus);

        assertEquals(primeRequest.getStatus(), newStatus);
    }

    private static Stream<Arguments> provideStatusValidPairs() {
        return Stream.of(
                Arguments.of(PrimeRequestStatus.PENDING, PrimeRequestStatus.PROCESSING),
                Arguments.of(PrimeRequestStatus.PROCESSING, PrimeRequestStatus.COMPLETED));
    }

    @Test
    public void testSetStatusInvalidInputException() {
        PrimeRequestStatus actualStatus = PrimeRequestStatus.PROCESSING;
        PrimeRequestStatus newStatus = PrimeRequestStatus.PENDING;
        primeRequest.setStatus(actualStatus);

        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
            primeRequest.setStatus(newStatus);
        });

        assertEquals("Cannot set status from %s to %s.".formatted(actualStatus, newStatus), exception.getMessage());
    }
}
