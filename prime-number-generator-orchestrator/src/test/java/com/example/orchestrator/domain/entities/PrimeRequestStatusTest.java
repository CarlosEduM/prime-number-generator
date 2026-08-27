package com.example.orchestrator.domain.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class PrimeRequestStatusTest {
    @ParameterizedTest
    @CsvSource({
        "0",
        "1",
    })
    public void testGetNextValidNext(int ordinal) {
        PrimeRequestStatus status = PrimeRequestStatus.values()[ordinal];
        assertEquals(1, status.getNext().compareTo(status));
    }
    
    @Test
    public void testGetNextNotValidNext() {
        PrimeRequestStatus status = PrimeRequestStatus.COMPLETED;
        assertThrows(IllegalStateException.class, () -> {
            status.getNext();
        });
    }
}
