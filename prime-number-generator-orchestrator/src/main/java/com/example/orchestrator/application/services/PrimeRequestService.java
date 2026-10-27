package com.example.orchestrator.application.services;

import java.util.UUID;

import org.jspecify.annotations.NonNull;

import com.example.orchestrator.application.dtos.PrimeRequestDTO;
import com.example.orchestrator.domain.entities.PrimeRequests;
import com.example.orchestrator.domain.pojos.PageableRequest;
import com.example.orchestrator.domain.pojos.PageableResult;
import com.example.orchestrator.domain.repositories.PrimeRequestRepository;

public class PrimeRequestService {
    private final PrimeRequestRepository primeRequestRepository;

    public PrimeRequestService(PrimeRequestRepository primeRequestRepository) {
        this.primeRequestRepository = primeRequestRepository;
    }

    public PrimeRequests requestPrimeNumberRange(@NonNull PrimeRequestDTO request) {
        PrimeRequests primeRequest = new PrimeRequests();
        primeRequest.setInputNumberRange(request.inputNumberMin(), request.inputNumberMax());

        return primeRequestRepository.save(primeRequest);
    }

    public PrimeRequests getPrimeRequest(@NonNull UUID id) {
        return primeRequestRepository.getById(id);
    }

    public PageableResult<PrimeRequests> getAllPrimeRequests(@NonNull PageableRequest pageableRequest) {
        return this.primeRequestRepository.findAll(pageableRequest);
    }
}
