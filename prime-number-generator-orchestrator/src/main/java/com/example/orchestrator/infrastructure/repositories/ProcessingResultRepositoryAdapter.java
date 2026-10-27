package com.example.orchestrator.infrastructure.repositories;

import java.util.UUID;

import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Repository;

import com.example.orchestrator.domain.entities.ProcessingResults;
import com.example.orchestrator.domain.exceptions.NotFoundException;
import com.example.orchestrator.domain.repositories.ProcessingResultRepository;

@Repository
public class ProcessingResultRepositoryAdapter implements ProcessingResultRepository {
    private final ProcessingResultsJpaRepository processingResultsJpaRepository;

    public ProcessingResultRepositoryAdapter(ProcessingResultsJpaRepository processingResultsJpaRepository) {
        this.processingResultsJpaRepository = processingResultsJpaRepository;
    }

    @Override
    public ProcessingResults getById(@NonNull UUID id) {
        return this.processingResultsJpaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Processing result with ID %s not found.".formatted(id)));
    }

    @Override
    public ProcessingResults save(@NonNull ProcessingResults processingResults) {
        return this.processingResultsJpaRepository.save(processingResults);
    }
}
