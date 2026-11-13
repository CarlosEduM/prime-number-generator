package com.example.processor.domain.pojos;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class PrimeGenerator {
    private static final BigInteger TWO = BigInteger.valueOf(2);
    private static final BigInteger ONE = BigInteger.ONE;

    public List<BigInteger> generate(BigInteger inputNumberMin, BigInteger inputNumberMax) {
        if (inputNumberMin == null || inputNumberMax == null)
            throw new IllegalArgumentException("Range bounds must not be null.");
        if (inputNumberMin.signum() < 0 || inputNumberMax.signum() < 0)
            throw new IllegalArgumentException("Range bounds must be non-negative.");
        if (inputNumberMin.compareTo(inputNumberMax) > 0)
            throw new IllegalArgumentException("Range minimum must not exceed range maximum.");

        List<BigInteger> primes = new ArrayList<>();
        BigInteger candidate = inputNumberMin.max(TWO);

        while (candidate.compareTo(inputNumberMax) <= 0) {
            if (isPrime(candidate))
                primes.add(candidate);
            candidate = candidate.add(ONE);
        }

        return List.copyOf(primes);
    }

    private boolean isPrime(BigInteger candidate) {
        for (BigInteger divisor = TWO;
                divisor.multiply(divisor).compareTo(candidate) <= 0;
                divisor = divisor.add(ONE)) {
            if (candidate.remainder(divisor).signum() == 0)
                return false;
        }

        return true;
    }
}
