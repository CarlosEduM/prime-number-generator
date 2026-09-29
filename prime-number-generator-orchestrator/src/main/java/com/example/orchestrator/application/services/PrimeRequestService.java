package com.example.orchestrator.application.services;

import java.util.UUID;

import org.jspecify.annotations.NonNull;

import com.example.orchestrator.application.dtos.PrimeRequestDTO;
import com.example.orchestrator.application.dtos.PrimeResponseDTO;
import com.example.orchestrator.application.mapper.PrimeRequestMapper;
import com.example.orchestrator.domain.entities.PrimeRequest;
import com.example.orchestrator.domain.pojos.PageableRequest;
import com.example.orchestrator.domain.pojos.PageableResult;
import com.example.orchestrator.domain.repositories.PrimeRequestRepository;

public class PrimeRequestService {
    private PrimeRequestRepository primeRequestRepository;
    private PrimeRequestMapper primeRequestMapper;

    public PrimeRequestService(PrimeRequestRepository primeRequestRepository,
            PrimeRequestMapper primeRequestMapper) {
        this.primeRequestRepository = primeRequestRepository;
        this.primeRequestMapper = primeRequestMapper;
    }

    public PrimeRequest requestPrimeNumberRange(@NonNull PrimeRequestDTO request) {
        PrimeRequest primeRequest = new PrimeRequest();
        primeRequest.setInputNumberRange(request.inputNumberMin(), request.inputNumberMax());

        return primeRequestRepository.save(primeRequest);
    }

    public PrimeResponseDTO getPrimeRequest(@NonNull UUID id) {
        PrimeRequest request = primeRequestRepository.getById(id);

        return this.primeRequestMapper.toDTO(request);
    }

    public PageableResult<PrimeResponseDTO> getAllPrimeRequests(@NonNull PageableRequest pageableRequest) {
        PageableResult<PrimeRequest> page = this.primeRequestRepository.findAll(pageableRequest);

        return page.map((entity) -> this.primeRequestMapper.toDTO(entity));
    }
}
