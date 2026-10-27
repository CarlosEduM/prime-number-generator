package com.example.orchestrator.infrastructure.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.orchestrator.domain.entities.PrimeRequests;

public interface PrimeRequestJpaRepository
        extends JpaRepository<PrimeRequests, UUID> {
}
