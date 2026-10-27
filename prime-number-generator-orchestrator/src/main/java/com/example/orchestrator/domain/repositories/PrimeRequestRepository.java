package com.example.orchestrator.domain.repositories;

import java.util.UUID;

import org.jspecify.annotations.NonNull;

import com.example.orchestrator.domain.entities.PrimeRequests;
import com.example.orchestrator.domain.pojos.PageableRequest;
import com.example.orchestrator.domain.pojos.PageableResult;

public interface PrimeRequestRepository {
    PrimeRequests save(@NonNull PrimeRequests primeRequest);

    PrimeRequests getByIdAndOwnerId(@NonNull UUID id, @NonNull String ownerId);

    PageableResult<PrimeRequests> findAllByOwnerId(@NonNull String ownerId, @NonNull PageableRequest request);
}
