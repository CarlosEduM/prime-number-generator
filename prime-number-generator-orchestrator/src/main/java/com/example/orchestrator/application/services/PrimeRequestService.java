package com.example.orchestrator.application.services;

import java.util.UUID;

import org.jspecify.annotations.NonNull;

import com.example.orchestrator.application.dtos.PrimeRequestDTO;
import com.example.orchestrator.domain.entities.PrimeRequest;
import com.example.orchestrator.domain.pojos.PageableRequest;
import com.example.orchestrator.domain.pojos.PageableResult;
import com.example.orchestrator.domain.repositories.PrimeRequestRepository;

public class PrimeRequestService {
    private PrimeRequestRepository primeRequestRepository;

    public PrimeRequestService(PrimeRequestRepository primeRequestRepository) {
        this.primeRequestRepository = primeRequestRepository;
    }

    public PrimeRequest requestPrimeNumberRange(@NonNull PrimeRequestDTO request) {
        PrimeRequest primeRequest = new PrimeRequest();
        primeRequest.setInputNumberRange(request.inputNumberMin(), request.inputNumberMax());

        return primeRequestRepository.save(primeRequest);
    }

    public PrimeRequest getPrimeRequest(@NonNull UUID id) {
        return primeRequestRepository.getById(id);
    }

    public PageableResult<PrimeRequest> getAllPrimeRequests(@NonNull PageableRequest pageableRequest) {
        return this.primeRequestRepository.findAll(pageableRequest);
    }
}
