package com.example.orchestrator.infrastructure.repositories;

import java.util.UUID;

import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.example.orchestrator.domain.entities.PrimeRequests;
import com.example.orchestrator.domain.exceptions.NotFoundException;
import com.example.orchestrator.domain.pojos.PageableRequest;
import com.example.orchestrator.domain.pojos.PageableResult;
import com.example.orchestrator.domain.repositories.PrimeRequestRepository;

@Repository
public class PrimeRequestRepositoryAdapter implements PrimeRequestRepository {
    private final PrimeRequestJpaRepository primeRequestJpaRepository;

    public PrimeRequestRepositoryAdapter(PrimeRequestJpaRepository primeRequestJpaRepository) {
        this.primeRequestJpaRepository = primeRequestJpaRepository;
    }

    @Override
    public PrimeRequests save(@NonNull PrimeRequests primeRequest) {
        return this.primeRequestJpaRepository.save(primeRequest);
    }

    @Override
    public PrimeRequests getById(@NonNull UUID id) {
        return this.primeRequestJpaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Prime request with ID %s not found.".formatted(id)));
    }

    @Override
    public PageableResult<PrimeRequests> findAll(@NonNull PageableRequest request) {
        Sort sort = Sort.by(request.sorts().stream()
                .map(order -> new Sort.Order(
                        order.direction() == PageableRequest.Direction.ASC ? Sort.Direction.ASC : Sort.Direction.DESC,
                        order.property()))
                .toList());
        Page<PrimeRequests> page = this.primeRequestJpaRepository.findAll(
                PageRequest.of(request.page(), request.size(), sort));

        return new PageableResult<>(page.getContent(), page.getNumber(), page.getSize(), page.getTotalElements());
    }
}
