package com.example.orchestrator.domain.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.example.orchestrator.domain.exceptions.IllegalOperationException;

public class ProcessingResultsTest {
    private ProcessingResults processingResults;

    @BeforeEach
    public void setUp() {
        processingResults = new ProcessingResults();
    }

    @Test
    public void testUpdateToProcessingValidValues() {
        LocalDateTime startedAt = LocalDateTime.now();

        processingResults.updateToProcessing(startedAt);

        assertEquals(ProcessingResultState.PROCESSING, processingResults.getState());
        assertEquals(startedAt, processingResults.getStartedAt());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidStatesForUpdateToProcessing")
    public void testUpdateToProcessingInvalidStates(ProcessingResultState currentState)
            throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        LocalDateTime startedAt = LocalDateTime.now();

        this.setProcessingResultsState(currentState);

        assertThrows(IllegalOperationException.class, () -> {
            processingResults.updateToProcessing(startedAt);
        });
    }

    private void setProcessingResultsState(ProcessingResultState processing)
            throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field stateField = ProcessingResults.class.getDeclaredField("state");
        stateField.setAccessible(true);
        stateField.set(this.processingResults, processing);
    }

    private static Stream<Arguments> provideInvalidStatesForUpdateToProcessing() {
        return Stream.of(
                Arguments.of(ProcessingResultState.PROCESSING),
                Arguments.of(ProcessingResultState.SUCCESS),
                Arguments.of(ProcessingResultState.FAIL));
    }

    @Test
    public void testUpdateToSuccessValidValues() {
        List<BigInteger> results = List.of(BigInteger.valueOf(2), BigInteger.valueOf(3));
        LocalDateTime finishedAt = LocalDateTime.now();

        processingResults.updateToProcessing(LocalDateTime.now());
        processingResults.updateToSuccess(results, finishedAt);

        assertEquals(ProcessingResultState.SUCCESS, processingResults.getState());
        assertEquals(results, processingResults.getResults());
        assertEquals(finishedAt, processingResults.getFinishedAt());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidStatesForUpdateToSuccess")
    public void testUpdateToSuccessInvalidStates(ProcessingResultState currentState)
            throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        List<BigInteger> results = List.of(BigInteger.valueOf(2), BigInteger.valueOf(3));
        LocalDateTime finishedAt = LocalDateTime.now();
        this.setProcessingResultsState(currentState);

        assertThrows(IllegalOperationException.class, () -> {
            processingResults.updateToSuccess(results, finishedAt);
        });
    }

    private static Stream<Arguments> provideInvalidStatesForUpdateToSuccess() {
        return Stream.of(
                Arguments.of(ProcessingResultState.IN_QUEUE),
                Arguments.of(ProcessingResultState.SUCCESS),
                Arguments.of(ProcessingResultState.FAIL));
    }

    @Test
    public void testUpdateToFailValidValues() {
        String errorMessage = "An error occurred";
        LocalDateTime finishedAt = LocalDateTime.now();

        processingResults.updateToProcessing(LocalDateTime.now());
        processingResults.updateToFail(errorMessage, finishedAt);

        assertEquals(ProcessingResultState.FAIL, processingResults.getState());
        assertEquals(errorMessage, processingResults.getErrorMessage());
        assertEquals(finishedAt, processingResults.getFinishedAt());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidStatesForUpdateToFail")
    public void testUpdateToFailInvalidStates(ProcessingResultState currentState)
            throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        String errorMessage = "An error occurred";
        LocalDateTime finishedAt = LocalDateTime.now();
        this.setProcessingResultsState(currentState);

        assertThrows(IllegalOperationException.class, () -> {
            processingResults.updateToFail(errorMessage, finishedAt);
        });
    }

    private static Stream<Arguments> provideInvalidStatesForUpdateToFail() {
        return Stream.of(
                Arguments.of(ProcessingResultState.IN_QUEUE),
                Arguments.of(ProcessingResultState.SUCCESS),
                Arguments.of(ProcessingResultState.FAIL));
    }
}
