package com.example.orchestrator.application.mapper;

import com.example.orchestrator.application.dtos.PrimeResponseDTO;
import com.example.orchestrator.domain.entities.PrimeRequest;

public interface PrimeRequestMapper {
    PrimeResponseDTO toDTO(PrimeRequest entity);
}
