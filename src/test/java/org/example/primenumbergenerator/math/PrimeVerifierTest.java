package org.example.primenumbergenerator.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PrimeVerifierTest {
    private final PrimeVerifier primeVerifier = new PrimeVerifier();

    @Test
    void isAPrimeNumberTest_happyPath() {
        assertTrue(primeVerifier.isAPrimeNumber(5L));
    }

    @Test
    void isAPrimeNumberTest_notPrimeNumber() {
        assertFalse(primeVerifier.isAPrimeNumber(10L));
    }

    @Test
    void isAPrimeNumberTest_negativeNumber() {
        assertFalse(primeVerifier.isAPrimeNumber(-7L));
    }

    @Test
    void isAPrimeNumberTest_numberOne() {
        assertFalse(primeVerifier.isAPrimeNumber(1L));
    }
}