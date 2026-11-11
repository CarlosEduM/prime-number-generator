package com.example.orchestrator.application.services;

import java.util.UUID;

import org.jspecify.annotations.NonNull;

import com.example.orchestrator.application.dtos.PrimeNumberRangeMessage;
import com.example.orchestrator.application.dtos.PrimeRequestDTO;
import com.example.orchestrator.application.messaging.PrimeRequestMessageProducer;
import com.example.orchestrator.domain.entities.PrimeRequests;
import com.example.orchestrator.domain.exceptions.InvalidInputException;
import com.example.orchestrator.domain.pojos.PageableRequest;
import com.example.orchestrator.domain.pojos.PageableResult;
import com.example.orchestrator.domain.repositories.PrimeRequestRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j 
public class PrimeRequestService {
    private final PrimeRequestRepository primeRequestRepository;
    private final PrimeRequestMessageProducer primeRequestMessageProducer;

    public PrimeRequestService(
            PrimeRequestRepository primeRequestRepository,
            PrimeRequestMessageProducer primeRequestMessageProducer) {
        this.primeRequestRepository = primeRequestRepository;
        this.primeRequestMessageProducer = primeRequestMessageProducer;
    }

    public PrimeRequests requestPrimeNumberRange(@NonNull PrimeRequestDTO request, @NonNull String ownerId) {
        log.info("Received request to generate prime numbers in range {} - {} for owner {}",
                request.inputNumberMin(), request.inputNumberMax(), ownerId);
        PrimeRequests primeRequest = new PrimeRequests();
        primeRequest.assignOwnerId(requireOwnerId(ownerId));
        primeRequest.setInputNumberRange(request.inputNumberMin(), request.inputNumberMax());

        PrimeRequests savedRequest = primeRequestRepository.save(primeRequest);
        primeRequestMessageProducer.produce(
                savedRequest.getResults().getId(),
                new PrimeNumberRangeMessage(savedRequest.getInputNumberMin(), savedRequest.getInputNumberMax()));

        return savedRequest;
    }

    public PrimeRequests getPrimeRequest(@NonNull UUID id, @NonNull String ownerId) {
        log.info("Getting prime request with ID {} for owner {}", id, ownerId);
        return primeRequestRepository.getByIdAndOwnerId(id, requireOwnerId(ownerId));
    }

    public PageableResult<PrimeRequests> getAllPrimeRequests(
            @NonNull PageableRequest pageableRequest, @NonNull String ownerId) {
        log.info("Getting page {} with size {} and sorts {} for owner {}", pageableRequest.page(), 
        pageableRequest.size(), pageableRequest.sorts(), ownerId);
        return this.primeRequestRepository.findAllByOwnerId(requireOwnerId(ownerId), pageableRequest);
    }

    private String requireOwnerId(String ownerId) {
        if (ownerId == null || ownerId.isBlank())
            throw new InvalidInputException("Owner ID must not be blank.");

        return ownerId;
    }
}
