package com.example.orchestrator.application.services;

import java.util.UUID;

import org.jspecify.annotations.NonNull;

import com.example.orchestrator.application.dtos.PrimeRequestDTO;
import com.example.orchestrator.domain.entities.PrimeRequests;
import com.example.orchestrator.domain.exceptions.InvalidInputException;
import com.example.orchestrator.domain.pojos.PageableRequest;
import com.example.orchestrator.domain.pojos.PageableResult;
import com.example.orchestrator.domain.repositories.PrimeRequestRepository;

public class PrimeRequestService {
    private final PrimeRequestRepository primeRequestRepository;

    public PrimeRequestService(PrimeRequestRepository primeRequestRepository) {
        this.primeRequestRepository = primeRequestRepository;
    }

    public PrimeRequests requestPrimeNumberRange(@NonNull PrimeRequestDTO request, @NonNull String ownerId) {
        PrimeRequests primeRequest = new PrimeRequests();
        primeRequest.assignOwnerId(requireOwnerId(ownerId));
        primeRequest.setInputNumberRange(request.inputNumberMin(), request.inputNumberMax());

        return primeRequestRepository.save(primeRequest);
    }

    public PrimeRequests getPrimeRequest(@NonNull UUID id, @NonNull String ownerId) {
        return primeRequestRepository.getByIdAndOwnerId(id, requireOwnerId(ownerId));
    }

    public PageableResult<PrimeRequests> getAllPrimeRequests(
            @NonNull PageableRequest pageableRequest, @NonNull String ownerId) {
        return this.primeRequestRepository.findAllByOwnerId(requireOwnerId(ownerId), pageableRequest);
    }

    private String requireOwnerId(String ownerId) {
        if (ownerId == null || ownerId.isBlank())
            throw new InvalidInputException("Owner ID must not be blank.");

        return ownerId;
    }
}
