package com.example.orchestrator.application.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.orchestrator.application.dtos.ProcessingResultUpdateDTO;
import com.example.orchestrator.domain.entities.ProcessingResultState;
import com.example.orchestrator.domain.entities.ProcessingResults;
import com.example.orchestrator.domain.exceptions.IllegalOperationException;
import com.example.orchestrator.domain.repositories.ProcessingResultRepository;

@ExtendWith(MockitoExtension.class)
class ProcessingResultServiceTest {
    @Mock
    private ProcessingResultRepository repository;

    private ProcessingResultService service;

    @BeforeEach
    void setUp() {
        service = new ProcessingResultService(repository);
    }

    @Test
    void testUpdateToProcessingTransitionsAndSavesResult() {
        UUID id = UUID.randomUUID();
        LocalDateTime startedAt = LocalDateTime.of(2026, 10, 27, 8, 0);
        ProcessingResults processingResults = new ProcessingResults();
        when(repository.getById(id)).thenReturn(processingResults);
        when(repository.save(processingResults)).thenReturn(processingResults);

        ProcessingResults actual = service.updateProcessingResult(
                id,
                new ProcessingResultUpdateDTO(ProcessingResultState.PROCESSING, startedAt, null, null, null));

        assertSame(processingResults, actual);
        assertEquals(ProcessingResultState.PROCESSING, actual.getState());
        assertEquals(startedAt, actual.getStartedAt());
        verify(repository).save(processingResults);
    }

    @Test
    void testUpdateToSuccessTransitionsAndSavesResult() {
        UUID id = UUID.randomUUID();
        List<BigInteger> results = List.of(BigInteger.TWO, BigInteger.valueOf(3));
        LocalDateTime finishedAt = LocalDateTime.of(2026, 10, 27, 9, 0);
        ProcessingResults processingResults = new ProcessingResults();
        processingResults.updateToProcessing(finishedAt.minusHours(1));
        when(repository.getById(id)).thenReturn(processingResults);
        when(repository.save(processingResults)).thenReturn(processingResults);

        ProcessingResults actual = service.updateProcessingResult(
                id,
                new ProcessingResultUpdateDTO(ProcessingResultState.SUCCESS, null, results, null, finishedAt));

        assertEquals(ProcessingResultState.SUCCESS, actual.getState());
        assertEquals(results, actual.getResults());
        assertEquals(finishedAt, actual.getFinishedAt());
        verify(repository).save(processingResults);
    }

    @Test
    void testUpdateToFailTransitionsAndSavesResult() {
        UUID id = UUID.randomUUID();
        String errorMessage = "Processor failed";
        LocalDateTime finishedAt = LocalDateTime.of(2026, 10, 27, 9, 0);
        ProcessingResults processingResults = new ProcessingResults();
        processingResults.updateToProcessing(finishedAt.minusHours(1));
        when(repository.getById(id)).thenReturn(processingResults);
        when(repository.save(processingResults)).thenReturn(processingResults);

        ProcessingResults actual = service.updateProcessingResult(
                id,
                new ProcessingResultUpdateDTO(ProcessingResultState.FAIL, null, null, errorMessage, finishedAt));

        assertEquals(ProcessingResultState.FAIL, actual.getState());
        assertEquals(errorMessage, actual.getErrorMessage());
        assertEquals(finishedAt, actual.getFinishedAt());
        verify(repository).save(processingResults);
    }

    @Test
    void testInvalidTransitionDoesNotSaveResult() {
        UUID id = UUID.randomUUID();
        ProcessingResults processingResults = new ProcessingResults();
        when(repository.getById(id)).thenReturn(processingResults);
        ProcessingResultUpdateDTO updateDTO = new ProcessingResultUpdateDTO(
                ProcessingResultState.SUCCESS,
                null,
                List.of(BigInteger.TWO),
                null,
                LocalDateTime.of(2026, 10, 27, 9, 0));

        assertThrows(IllegalOperationException.class, () -> service.updateProcessingResult(id, updateDTO));

        verify(repository, never()).save(processingResults);
    }
}
