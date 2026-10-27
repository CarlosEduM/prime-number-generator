package com.example.orchestrator.application.services;

import java.util.UUID;

import org.jspecify.annotations.NonNull;

import com.example.orchestrator.application.dtos.ProcessingResultUpdateDTO;
import com.example.orchestrator.domain.entities.ProcessingResults;
import com.example.orchestrator.domain.entities.ProcessingResultState;
import com.example.orchestrator.domain.repositories.ProcessingResultRepository;

public class ProcessingResultService {
    private final ProcessingResultRepository processingResultRepository;

    public ProcessingResultService(ProcessingResultRepository processingResultRepository) {
        this.processingResultRepository = processingResultRepository;
    }

    public ProcessingResults updateProcessingResult(
            @NonNull UUID id,
            @NonNull ProcessingResultUpdateDTO updateDTO) {
        ProcessingResults processingResults = this.processingResultRepository.getById(id);

        switch (updateDTO.state()) {
            case PROCESSING -> processingResults.updateToProcessing(updateDTO.startedAt());
            case SUCCESS -> processingResults.updateToSuccess(updateDTO.results(), updateDTO.finishedAt());
            case FAIL -> processingResults.updateToFail(updateDTO.errorMessage(), updateDTO.finishedAt());
            case IN_QUEUE -> throw new IllegalArgumentException(
                    "%s is not a valid update state.".formatted(ProcessingResultState.IN_QUEUE));
        }

        return this.processingResultRepository.save(processingResults);
    }
}
