package com.example.orchestrator.domain.repositories;

import java.util.UUID;

import org.jspecify.annotations.NonNull;

import com.example.orchestrator.domain.entities.PrimeRequest;
import com.example.orchestrator.domain.pojos.PageableRequest;
import com.example.orchestrator.domain.pojos.PageableResult;

public interface PrimeRequestRepository {
    PrimeRequest save(@NonNull PrimeRequest primeRequest);

    PrimeRequest getById(@NonNull UUID id);

    PageableResult<PrimeRequest> findAll(PageableRequest request);
}
