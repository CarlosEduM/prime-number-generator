package com.example.processor.domain.pojos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigInteger;
import java.util.List;

import org.junit.jupiter.api.Test;

class PrimeGeneratorTest {
    private final PrimeGenerator primeGenerator = new PrimeGenerator();

    @Test
    void testGenerateReturnsPrimesWithinInclusiveRange() {
        List<BigInteger> primes = primeGenerator.generate(BigInteger.ZERO, BigInteger.TEN);

        assertEquals(List.of(
                BigInteger.valueOf(2),
                BigInteger.valueOf(3),
                BigInteger.valueOf(5),
                BigInteger.valueOf(7)), primes);
    }

    @Test
    void testGenerateIncludesPrimeAtSingleValueRange() {
        List<BigInteger> primes = primeGenerator.generate(BigInteger.valueOf(11), BigInteger.valueOf(11));

        assertEquals(List.of(BigInteger.valueOf(11)), primes);
    }

    @Test
    void testGenerateReturnsEmptyListWhenRangeContainsNoPrimes() {
        List<BigInteger> primes = primeGenerator.generate(BigInteger.ZERO, BigInteger.ONE);

        assertEquals(List.of(), primes);
    }

    @Test
    void testGenerateHandlesValuesBeyondLongRange() {
        BigInteger valueBeyondLongRange = BigInteger.ONE.shiftLeft(64);

        List<BigInteger> primes = primeGenerator.generate(valueBeyondLongRange, valueBeyondLongRange);

        assertEquals(List.of(), primes);
    }

    @Test
    void testGenerateRejectsNullBounds() {
        assertThrows(IllegalArgumentException.class, () -> primeGenerator.generate(null, BigInteger.TEN));
        assertThrows(IllegalArgumentException.class, () -> primeGenerator.generate(BigInteger.ZERO, null));
    }

    @Test
    void testGenerateRejectsNegativeBounds() {
        assertThrows(IllegalArgumentException.class,
                () -> primeGenerator.generate(BigInteger.valueOf(-1), BigInteger.TEN));
    }

    @Test
    void testGenerateRejectsReversedRange() {
        assertThrows(IllegalArgumentException.class,
                () -> primeGenerator.generate(BigInteger.TEN, BigInteger.ZERO));
    }
}
