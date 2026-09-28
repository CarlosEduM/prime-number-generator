package com.example.orchestrator.domain.entities;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.jspecify.annotations.NonNull;

import com.example.orchestrator.domain.exceptions.IllegalOperationException;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;

@Entity
@Getter
@Table(name = "processing_results", uniqueConstraints = @UniqueConstraint(columnNames = { "request_id",
        "processor_name" }))
public class ProcessingResults {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Enumerated(EnumType.STRING)
    private ProcessingResultState state = ProcessingResultState.IN_QUEUE;
    private String errorMessage;
    @JdbcTypeCode(SqlTypes.JSON)
    private List<BigInteger> results;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;

    public void updateToProcessing(@NonNull LocalDateTime startedAt) {
        ProcessingResultState newState = ProcessingResultState.PROCESSING;
        this.validateTransictionOrThrow(ProcessingResultState.IN_QUEUE, newState);

        this.state = newState;
        this.startedAt = startedAt;
    }

    private void validateTransictionOrThrow(ProcessingResultState expectedState, ProcessingResultState newState) {
        if (!this.state.equals(expectedState))
            throw new IllegalOperationException(
                    "Cannot set state from %s to %s.".formatted(this.state, newState));

    }

    public void updateToSuccess(@NonNull List<BigInteger> results, @NonNull LocalDateTime finishedAt) {
        ProcessingResultState newState = ProcessingResultState.SUCCESS;
        this.validateTransictionOrThrow(ProcessingResultState.PROCESSING, newState);

        this.state = newState;
        this.results = results;
        this.finishedAt = finishedAt;
    }

    public void updateToFail(@NonNull String errorMessage, @NonNull LocalDateTime finishedAt) {
        ProcessingResultState newState = ProcessingResultState.FAIL;
        this.validateTransictionOrThrow(ProcessingResultState.PROCESSING, newState);

        this.state = newState;
        this.errorMessage = errorMessage;
        this.finishedAt = finishedAt;
    }
}
