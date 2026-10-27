package com.example.orchestrator.infrastructure.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.orchestrator.domain.entities.ProcessingResults;

public interface ProcessingResultsJpaRepository extends JpaRepository<ProcessingResults, UUID> {
}
