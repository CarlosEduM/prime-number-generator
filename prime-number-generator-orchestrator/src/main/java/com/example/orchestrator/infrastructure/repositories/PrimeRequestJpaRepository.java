package com.example.orchestrator.infrastructure.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.orchestrator.domain.entities.PrimeRequests;

public interface PrimeRequestJpaRepository
        extends JpaRepository<PrimeRequests, UUID> {
    Optional<PrimeRequests> findByIdAndOwnerId(UUID id, String ownerId);

    Page<PrimeRequests> findAllByOwnerId(String ownerId, Pageable pageable);
}
