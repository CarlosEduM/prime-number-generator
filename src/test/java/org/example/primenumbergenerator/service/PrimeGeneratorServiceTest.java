package org.example.primenumbergenerator.service;

import org.example.primenumbergenerator.exception.PrimeGeneratorException;
import org.example.primenumbergenerator.math.PrimeVerifier;
import org.example.primenumbergenerator.service.impl.PrimeGeneratorServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PrimeGeneratorServiceTest {
    static PrimeGeneratorService service;
    static PrimeVerifier verifier;

    @BeforeAll
    static void beforeAll() {
        verifier = new PrimeVerifier();
        service = new PrimeGeneratorServiceImpl(verifier);
    }

    @Test
    void generateAPrimeList_happyPath() {
        List<Long> primeList = service.generatePrimeListWithLengthLimit(5);

        assertEquals(Arrays.asList(2L, 3L, 5L, 7L, 11L), primeList);
    }

    @Test
    void generateAPrimeList_numberSmallerThanOne() {
        assertThrows(PrimeGeneratorException.class, () -> service.generatePrimeListWithLengthLimit(0));
    }

    @Test
    void generateAPrimeListUpTo_happyPath() {
        List<Long> primeList = service.generatePrimeListWithLargestNumberLimit(15L);

        assertEquals(Arrays.asList(2L, 3L, 5L, 7L, 11L, 13L), primeList);
    }

    @Test
    void generateAPrimeListUpTo_numberSmallerThanTwo() {
        assertThrows(PrimeGeneratorException.class, () -> service.generatePrimeListWithLargestNumberLimit(1L));
    }

    @Test
    void generateTheLargestPrimeNumberUpTo_happyPath() {
        Long primeNumber = service.generateLargestPrime(15L);

        assertEquals(13L, primeNumber);
    }

    @Test
    void generateTheLargestPrimeNumberUpTo_numberSmallerThanTwo() {
        assertThrows(PrimeGeneratorException.class, () -> service.generateLargestPrime(1L));
    }
}