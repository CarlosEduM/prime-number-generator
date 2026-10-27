package com.example.orchestrator.application.dtos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.example.orchestrator.domain.entities.ProcessingResultState;

class ProcessingResultUpdateDTOTest {
    @Test
    void testSuccessUpdateCopiesResults() {
        List<BigInteger> results = new ArrayList<>(List.of(BigInteger.TWO, BigInteger.valueOf(3)));
        LocalDateTime finishedAt = LocalDateTime.of(2026, 10, 27, 9, 0);

        ProcessingResultUpdateDTO updateDTO = new ProcessingResultUpdateDTO(
                ProcessingResultState.SUCCESS,
                null,
                results,
                null,
                finishedAt);
        results.add(BigInteger.valueOf(5));

        assertEquals(List.of(BigInteger.TWO, BigInteger.valueOf(3)), updateDTO.results());
        assertEquals(finishedAt, updateDTO.finishedAt());
    }

    @Test
    void testProcessingUpdateRequiresOnlyStartedAt() {
        LocalDateTime startedAt = LocalDateTime.of(2026, 10, 27, 8, 0);

        ProcessingResultUpdateDTO updateDTO = new ProcessingResultUpdateDTO(
                ProcessingResultState.PROCESSING,
                startedAt,
                null,
                null,
                null);

        assertEquals(startedAt, updateDTO.startedAt());
    }

    @Test
    void testFailUpdateRequiresErrorMessageAndFinishedAt() {
        String errorMessage = "Processor failed";
        LocalDateTime finishedAt = LocalDateTime.of(2026, 10, 27, 9, 0);

        ProcessingResultUpdateDTO updateDTO = new ProcessingResultUpdateDTO(
                ProcessingResultState.FAIL,
                null,
                null,
                errorMessage,
                finishedAt);

        assertEquals(errorMessage, updateDTO.errorMessage());
        assertEquals(finishedAt, updateDTO.finishedAt());
    }

    @Test
    void testUpdateRejectsMissingOrIrrelevantParameters() {
        assertThrows(IllegalArgumentException.class, () -> new ProcessingResultUpdateDTO(
                ProcessingResultState.PROCESSING, null, null, null, null));
        assertThrows(IllegalArgumentException.class, () -> new ProcessingResultUpdateDTO(
                ProcessingResultState.SUCCESS, null, null, null, LocalDateTime.now()));
        assertThrows(IllegalArgumentException.class, () -> new ProcessingResultUpdateDTO(
                ProcessingResultState.SUCCESS, LocalDateTime.now(), List.of(BigInteger.TWO), null,
                LocalDateTime.now()));
        assertThrows(IllegalArgumentException.class, () -> new ProcessingResultUpdateDTO(
                ProcessingResultState.FAIL, null, null, null, LocalDateTime.now()));
        assertThrows(IllegalArgumentException.class, () -> new ProcessingResultUpdateDTO(
                ProcessingResultState.IN_QUEUE, null, null, null, null));
    }
}
