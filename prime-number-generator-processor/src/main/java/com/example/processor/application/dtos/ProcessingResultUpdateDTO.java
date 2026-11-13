package com.example.processor.application.dtos;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

public record ProcessingResultUpdateDTO(
        ProcessingResultUpdateState state,
        LocalDateTime startedAt,
        List<BigInteger> results,
        String errorMessage,
        LocalDateTime finishedAt) {

    public ProcessingResultUpdateDTO {
        if (state == null)
            throw new IllegalArgumentException("State is required.");

        switch (state) {
            case PROCESSING -> {
                requirePresent(startedAt, "startedAt");
                requireAbsent(results, "results");
                requireAbsent(errorMessage, "errorMessage");
                requireAbsent(finishedAt, "finishedAt");
            }
            case SUCCESS -> {
                requireAbsent(startedAt, "startedAt");
                requirePresent(results, "results");
                requireAbsent(errorMessage, "errorMessage");
                requirePresent(finishedAt, "finishedAt");
                results = List.copyOf(results);
            }
            case FAIL -> {
                requireAbsent(startedAt, "startedAt");
                requireAbsent(results, "results");
                requirePresent(errorMessage, "errorMessage");
                requirePresent(finishedAt, "finishedAt");
            }
        }
    }

    public static ProcessingResultUpdateDTO processing(LocalDateTime startedAt) {
        return new ProcessingResultUpdateDTO(ProcessingResultUpdateState.PROCESSING, startedAt, null, null, null);
    }

    public static ProcessingResultUpdateDTO success(List<BigInteger> results, LocalDateTime finishedAt) {
        return new ProcessingResultUpdateDTO(ProcessingResultUpdateState.SUCCESS, null, results, null, finishedAt);
    }

    public static ProcessingResultUpdateDTO fail(String errorMessage, LocalDateTime finishedAt) {
        return new ProcessingResultUpdateDTO(ProcessingResultUpdateState.FAIL, null, null, errorMessage, finishedAt);
    }

    private static void requirePresent(Object value, String fieldName) {
        if (value == null)
            throw new IllegalArgumentException("%s is required for the selected state.".formatted(fieldName));
    }

    private static void requireAbsent(Object value, String fieldName) {
        if (value != null)
            throw new IllegalArgumentException("%s is not valid for the selected state.".formatted(fieldName));
    }
}
