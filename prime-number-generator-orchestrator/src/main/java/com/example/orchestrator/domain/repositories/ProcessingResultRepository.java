package com.example.orchestrator.domain.repositories;

import java.util.UUID;

import org.jspecify.annotations.NonNull;

import com.example.orchestrator.domain.entities.ProcessingResults;

public interface ProcessingResultRepository {
    ProcessingResults getById(@NonNull UUID id);

    ProcessingResults save(@NonNull ProcessingResults processingResults);
}
