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
        List<Long> primeList = service.generateAPrimeList(5);

        assertEquals(Arrays.asList(2L, 3L, 5L, 7L, 11L), primeList);
    }

    @Test
    void generateAPrimeList_numberSmallerThanOne() {
        assertThrows(PrimeGeneratorException.class, () -> service.generateAPrimeList(0));
    }

    @Test
    void generateAPrimeListUpTo_happyPath() {
        List<Long> primeList = service.generateAPrimeListUpTo(15L);

        assertEquals(Arrays.asList(2L, 3L, 5L, 7L, 11L, 13L), primeList);
    }

    @Test
    void generateAPrimeListUpTo_numberSmallerThanTwo() {
        assertThrows(PrimeGeneratorException.class, () -> service.generateAPrimeListUpTo(1L));
    }

    @Test
    void generateTheLargestPrimeNumberUpTo_happyPath() {
        Long primeNumber = service.generateTheLargestPrimeNumberUpTo(15L);

        assertEquals(13L, primeNumber);
    }

    @Test
    void generateTheLargestPrimeNumberUpTo_numberSmallerThanTwo() {
        assertThrows(PrimeGeneratorException.class, () -> service.generateTheLargestPrimeNumberUpTo(1L));
    }
}