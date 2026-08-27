package com.example.orchestrator.domain.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.orchestrator.domain.exceptions.BadInputException;

public class PrimeRequestsTest {
    private PrimeRequests primeRequests;

    @BeforeEach
    public void setUp() {
        primeRequests = new PrimeRequests();
    }

    @Test
    public void testSetInputNumberRangeValidValues() {
        BigInteger inputNumberMin = BigInteger.valueOf(0);
        BigInteger inputNumberMax = BigInteger.valueOf(10);
        LocalDateTime oldUpdatedAt = primeRequests.getUpdatedAt();

        primeRequests.setInputNumberRange(inputNumberMin, inputNumberMax);

        assertEquals(inputNumberMin, primeRequests.getInputNumberMin());
        assertEquals(inputNumberMax, primeRequests.getInputNumberMax());
        assertTrue(primeRequests.getUpdatedAt().isAfter(oldUpdatedAt));
    }

    @Test
    public void testSetInputNumberRangeInputNumberMinTooSmall() {
        BigInteger inputNumberMin = BigInteger.valueOf(-1);
        BigInteger inputNumberMax = BigInteger.valueOf(10);

        assertThrows(BadInputException.class, () -> {
            primeRequests.setInputNumberRange(inputNumberMin, inputNumberMax);
        });
    }

    @Test
    public void testSetInputNumberRangeInputNumberMinTooSmall2() {
        BigInteger inputNumberMin = BigInteger.valueOf(5);
        BigInteger inputNumberMax = BigInteger.valueOf(10);

        assertThrows(BadInputException.class, () -> {
            primeRequests.setInputNumberRange(inputNumberMin, inputNumberMax);
        });
    }

    @Test
    public void testSetInputNumberRangeInputNumberMaxTooSmall() {
        BigInteger inputNumberMin = BigInteger.valueOf(5);
        BigInteger inputNumberMax = BigInteger.valueOf(4);

        assertThrows(BadInputException.class, () -> {
            primeRequests.setInputNumberRange(inputNumberMin, inputNumberMax);
        });
    }

    @Test
    public void testSetInputNumberRangeInputNumberMinTooLarge() {
        BigInteger inputNumberMin = BigInteger.valueOf(1005);
        BigInteger inputNumberMax = BigInteger.valueOf(2006);

        assertThrows(BadInputException.class, () -> {
            primeRequests.setInputNumberRange(inputNumberMin, inputNumberMax);
        });
    }

    @Test
    public void testUpdateToNextStatusValidTransition() {
        LocalDateTime updatedAt = primeRequests.getUpdatedAt();
        PrimeRequestStatus status = primeRequests.getStatus();
        primeRequests.updateToNextStatus();

        assertEquals(1, primeRequests.getStatus().compareTo(status));
        assertTrue(primeRequests.getUpdatedAt().isAfter(updatedAt));
    }
}
