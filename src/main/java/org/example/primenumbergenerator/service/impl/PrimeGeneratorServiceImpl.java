package org.example.primenumbergenerator.service.impl;

import org.example.primenumbergenerator.exception.PrimeGeneratorException;
import org.example.primenumbergenerator.math.PrimeVerifier;
import org.example.primenumbergenerator.service.PrimeGeneratorService;

import java.util.List;
import java.util.Optional;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class PrimeGeneratorServiceImpl implements PrimeGeneratorService {
    public static final long MIN_PRIME_POSSIBLE = 2L;
    private final PrimeVerifier primeVerifier;

    public PrimeGeneratorServiceImpl(PrimeVerifier primeVerifier) {
        this.primeVerifier = primeVerifier;
    }

    @Override
    public List<Long> generatePrimeListWithLengthLimit(Integer length) {
        if (length < 1)
            throw new PrimeGeneratorException("The list length must be major than 0");

        return generatePrimes(MIN_PRIME_POSSIBLE, Long.MAX_VALUE, length).toList();
    }

    @Override
    public List<Long> generatePrimeListWithLargestNumberLimit(Long limit) {
        verifyLimit(limit);

        return generatePrimes(MIN_PRIME_POSSIBLE, limit, Integer.MAX_VALUE).toList();
    }

    @Override
    public Long generateLargestPrime(Long limit) {
        verifyLimit(limit);

        Optional<Long> prime = LongStream.iterate(limit, i -> i - 1)
                .filter(primeVerifier::isAPrimeNumber)
                .limit(Integer.MAX_VALUE)
                .boxed()
                .findFirst();

        return prime.get();
    }

    private void verifyLimit(Long limit) {
        if (limit < MIN_PRIME_POSSIBLE)
            throw new PrimeGeneratorException("The limit must be greater than 2");
    }

    private Stream<Long> generatePrimes(Long start, Long end, Integer length) {
        return LongStream.rangeClosed(start, end)
                .filter(primeVerifier::isAPrimeNumber)
                .limit(length)
                .boxed();
    }
}
