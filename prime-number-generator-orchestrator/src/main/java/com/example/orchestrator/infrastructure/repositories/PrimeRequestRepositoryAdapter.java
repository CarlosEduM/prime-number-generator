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
    public PrimeRequests getByIdAndOwnerId(@NonNull UUID id, @NonNull String ownerId) {
        return this.primeRequestJpaRepository.findByIdAndOwnerId(id, ownerId)
                .orElseThrow(() -> new NotFoundException("Prime request with ID %s not found.".formatted(id)));
    }

    @Override
    public PrimeRequests getByResultsId(@NonNull UUID resultsId) {
        return this.primeRequestJpaRepository.findByResults_Id(resultsId)
                .orElseThrow(() -> new NotFoundException(
                        "Prime request with processing result ID %s not found.".formatted(resultsId)));
    }

    @Override
    public PageableResult<PrimeRequests> findAllByOwnerId(
            @NonNull String ownerId, @NonNull PageableRequest request) {
        Sort sort = Sort.by(request.sorts().stream()
                .map(order -> new Sort.Order(
                        order.direction() == PageableRequest.Direction.ASC ? Sort.Direction.ASC : Sort.Direction.DESC,
                        order.property()))
                .toList());
        Page<PrimeRequests> page = this.primeRequestJpaRepository.findAllByOwnerId(
                ownerId, PageRequest.of(request.page(), request.size(), sort));

        return new PageableResult<>(page.getContent(), page.getNumber(), page.getSize(), page.getTotalElements());
    }
}
