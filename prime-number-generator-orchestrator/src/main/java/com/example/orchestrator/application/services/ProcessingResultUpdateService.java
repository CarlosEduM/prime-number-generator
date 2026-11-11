package com.example.orchestrator.application.services;

import java.util.UUID;

import org.jspecify.annotations.NonNull;

import com.example.orchestrator.application.dtos.ProcessingResultUpdateDTO;
import com.example.orchestrator.domain.entities.PrimeRequestStatus;
import com.example.orchestrator.domain.entities.PrimeRequests;
import com.example.orchestrator.domain.entities.ProcessingResults;
import com.example.orchestrator.domain.entities.ProcessingResultState;
import com.example.orchestrator.domain.repositories.PrimeRequestRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProcessingResultUpdateService {
    private final PrimeRequestRepository primeRequestRepository;

    public ProcessingResultUpdateService(PrimeRequestRepository primeRequestRepository) {
        this.primeRequestRepository = primeRequestRepository;
    }

    public PrimeRequests updateProcessingResult(
            @NonNull UUID id,
            @NonNull ProcessingResultUpdateDTO updateDTO) {
        PrimeRequests primeRequest = this.primeRequestRepository.getByResultsId(id);
        ProcessingResults processingResults = primeRequest.getResults();

        switch (updateDTO.state()) {
            case PROCESSING -> {
                log.info("Updating processing result with id {} to state PROCESSING", id);
                processingResults.updateToProcessing(updateDTO.startedAt());
                primeRequest.setStatus(PrimeRequestStatus.PROCESSING);
            }
            case SUCCESS -> {
                log.info("Updating processing result with id {} to state SUCCESS", id);
                processingResults.updateToSuccess(updateDTO.results(), updateDTO.finishedAt());
                primeRequest.setStatus(PrimeRequestStatus.COMPLETED);
            }
            case FAIL -> {
                log.info("Updating processing result with id {} to state FAIL", id);
                processingResults.updateToFail(updateDTO.errorMessage(), updateDTO.finishedAt());
                primeRequest.setStatus(PrimeRequestStatus.COMPLETED);
            }
            case IN_QUEUE -> throw new IllegalArgumentException(
                    "%s is not a valid update state.".formatted(ProcessingResultState.IN_QUEUE));
        }

        log.info("Saving updated prime request with id {} to database with status {}", primeRequest.getId(),
                primeRequest.getStatus());
        return this.primeRequestRepository.save(primeRequest);
    }
}
