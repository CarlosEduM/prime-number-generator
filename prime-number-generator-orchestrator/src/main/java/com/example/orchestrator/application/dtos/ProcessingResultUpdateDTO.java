package com.example.orchestrator.application.dtos;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

import com.example.orchestrator.domain.entities.ProcessingResultState;

public record ProcessingResultUpdateDTO(
        ProcessingResultState state,
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
            case IN_QUEUE -> throw new IllegalArgumentException("IN_QUEUE is not a valid update state.");
        }
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
