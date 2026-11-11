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
import com.example.orchestrator.domain.entities.PrimeRequestStatus;
import com.example.orchestrator.domain.entities.PrimeRequests;
import com.example.orchestrator.domain.entities.ProcessingResultState;
import com.example.orchestrator.domain.exceptions.IllegalOperationException;
import com.example.orchestrator.domain.repositories.PrimeRequestRepository;

@ExtendWith(MockitoExtension.class)
class ProcessingResultUpdateServiceTest {
    @Mock
    private PrimeRequestRepository repository;

    private ProcessingResultUpdateService service;

    @BeforeEach
    void setUp() {
        service = new ProcessingResultUpdateService(repository);
    }

    @Test
    void testUpdateToProcessingTransitionsAndSavesRequest() {
        UUID id = UUID.randomUUID();
        LocalDateTime startedAt = LocalDateTime.of(2026, 10, 27, 8, 0);
        PrimeRequests primeRequest = new PrimeRequests();
        when(repository.getByResultsId(id)).thenReturn(primeRequest);
        when(repository.save(primeRequest)).thenReturn(primeRequest);

        PrimeRequests actual = service.updateProcessingResult(
                id,
                new ProcessingResultUpdateDTO(ProcessingResultState.PROCESSING, startedAt, null, null, null));

        assertSame(primeRequest, actual);
        assertEquals(ProcessingResultState.PROCESSING, actual.getResults().getState());
        assertEquals(startedAt, actual.getResults().getStartedAt());
        assertEquals(PrimeRequestStatus.PROCESSING, actual.getStatus());
        verify(repository).save(primeRequest);
    }

    @Test
    void testUpdateToSuccessTransitionsAndCompletesRequest() {
        UUID id = UUID.randomUUID();
        List<BigInteger> results = List.of(BigInteger.TWO, BigInteger.valueOf(3));
        LocalDateTime finishedAt = LocalDateTime.of(2026, 10, 27, 9, 0);
        PrimeRequests primeRequest = new PrimeRequests();
        primeRequest.getResults().updateToProcessing(finishedAt.minusHours(1));
        primeRequest.setStatus(PrimeRequestStatus.PROCESSING);
        when(repository.getByResultsId(id)).thenReturn(primeRequest);
        when(repository.save(primeRequest)).thenReturn(primeRequest);

        PrimeRequests actual = service.updateProcessingResult(
                id,
                new ProcessingResultUpdateDTO(ProcessingResultState.SUCCESS, null, results, null, finishedAt));

        assertEquals(ProcessingResultState.SUCCESS, actual.getResults().getState());
        assertEquals(results, actual.getResults().getResults());
        assertEquals(finishedAt, actual.getResults().getFinishedAt());
        assertEquals(PrimeRequestStatus.COMPLETED, actual.getStatus());
        verify(repository).save(primeRequest);
    }

    @Test
    void testUpdateToFailTransitionsAndCompletesRequest() {
        UUID id = UUID.randomUUID();
        String errorMessage = "Processor failed";
        LocalDateTime finishedAt = LocalDateTime.of(2026, 10, 27, 9, 0);
        PrimeRequests primeRequest = new PrimeRequests();
        primeRequest.getResults().updateToProcessing(finishedAt.minusHours(1));
        primeRequest.setStatus(PrimeRequestStatus.PROCESSING);
        when(repository.getByResultsId(id)).thenReturn(primeRequest);
        when(repository.save(primeRequest)).thenReturn(primeRequest);

        PrimeRequests actual = service.updateProcessingResult(
                id,
                new ProcessingResultUpdateDTO(ProcessingResultState.FAIL, null, null, errorMessage, finishedAt));

        assertEquals(ProcessingResultState.FAIL, actual.getResults().getState());
        assertEquals(errorMessage, actual.getResults().getErrorMessage());
        assertEquals(finishedAt, actual.getResults().getFinishedAt());
        assertEquals(PrimeRequestStatus.COMPLETED, actual.getStatus());
        verify(repository).save(primeRequest);
    }

    @Test
    void testInvalidTransitionDoesNotSaveRequest() {
        UUID id = UUID.randomUUID();
        PrimeRequests primeRequest = new PrimeRequests();
        when(repository.getByResultsId(id)).thenReturn(primeRequest);
        ProcessingResultUpdateDTO updateDTO = new ProcessingResultUpdateDTO(
                ProcessingResultState.SUCCESS,
                null,
                List.of(BigInteger.TWO),
                null,
                LocalDateTime.of(2026, 10, 27, 9, 0));

        assertThrows(IllegalOperationException.class, () -> service.updateProcessingResult(id, updateDTO));

        verify(repository, never()).save(primeRequest);
    }
}
