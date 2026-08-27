package com.example.orchestrator.domain.repositories;

import com.example.orchestrator.domain.entities.PrimeRequests;

public interface PrimeRequestRepository {

    void save(PrimeRequests primeRequest);

}
