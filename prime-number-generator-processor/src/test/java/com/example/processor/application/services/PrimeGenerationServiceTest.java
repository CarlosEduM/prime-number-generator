package com.example.processor.application.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.processor.application.dtos.ProcessingResultUpdateDTO;
import com.example.processor.application.dtos.ProcessingResultUpdateState;
import com.example.processor.domain.pojos.PrimeGenerator;

class PrimeGenerationServiceTest {
    private static final UUID PROCESSING_RESULT_ID = UUID.fromString("6b3539e4-8c9d-4c0c-8e2c-f5b6959d04af");

    private PrimeGenerationService service;
    private List<ProcessingResultUpdateDTO> updates;

    @BeforeEach
    void setUp() {
        updates = new ArrayList<>();
        service = new PrimeGenerationService(new PrimeGenerator(), (key, message) -> {
            assertEquals(PROCESSING_RESULT_ID, key);
            updates.add(message);
        });
    }

    @Test
    void testGeneratePrimesPublishesProcessingAndSuccessUpdates() {
        List<BigInteger> expectedPrimes = List.of(BigInteger.valueOf(2), BigInteger.valueOf(3));

        List<BigInteger> primes = service.generatePrimes(PROCESSING_RESULT_ID, BigInteger.TWO, BigInteger.valueOf(3));

        assertEquals(expectedPrimes, primes);
        assertEquals(2, updates.size());
        assertEquals(ProcessingResultUpdateState.PROCESSING, updates.get(0).state());
        assertEquals(ProcessingResultUpdateState.SUCCESS, updates.get(1).state());
        assertNotNull(updates.get(0).startedAt());
        assertNotNull(updates.get(1).finishedAt());
        assertEquals(expectedPrimes, updates.get(1).results());
        assertNull(updates.get(0).results());
        assertNull(updates.get(0).errorMessage());
        assertNull(updates.get(0).finishedAt());
        assertNull(updates.get(1).startedAt());
        assertNull(updates.get(1).errorMessage());
    }

    @Test
    void testGeneratePrimesPublishesFailWhenRangeIsInvalid() {
        assertThrows(IllegalArgumentException.class,
                () -> service.generatePrimes(PROCESSING_RESULT_ID, BigInteger.TEN, BigInteger.ZERO));

        assertEquals(2, updates.size());
        assertEquals(ProcessingResultUpdateState.PROCESSING, updates.get(0).state());
        assertEquals(ProcessingResultUpdateState.FAIL, updates.get(1).state());
        assertNotNull(updates.get(0).startedAt());
        assertNotNull(updates.get(1).finishedAt());
        assertEquals("Range minimum must not exceed range maximum.", updates.get(1).errorMessage());
        assertNull(updates.get(1).results());
        assertNull(updates.get(1).startedAt());
        assertNull(updates.get(0).finishedAt());
        assertNull(updates.get(0).results());
        assertNull(updates.get(0).errorMessage());
    }
}
