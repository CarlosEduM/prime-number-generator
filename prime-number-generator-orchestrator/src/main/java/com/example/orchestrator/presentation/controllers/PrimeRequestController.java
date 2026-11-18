package com.example.orchestrator.presentation.controllers;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;

import com.example.orchestrator.application.dtos.PrimeRequestDTO;
import com.example.orchestrator.application.services.PrimeRequestService;
import com.example.orchestrator.domain.entities.PrimeRequests;
import com.example.orchestrator.domain.pojos.PageableRequest;
import com.example.orchestrator.domain.pojos.PageableResult;
import com.example.orchestrator.presentation.dtos.PrimeRequestResponseDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping("/api/v1/prime-requests")
@Validated
public class PrimeRequestController {
    private final PrimeRequestService primeRequestService;

    public PrimeRequestController(PrimeRequestService primeRequestService) {
        this.primeRequestService = primeRequestService;
    }

    @PostMapping
    public ResponseEntity<UUID> createPrimeRequest(
            @Valid @RequestBody PrimeRequestDTO request,
            @AuthenticationPrincipal Jwt jwt) {
        UUID createdRequestId = primeRequestService
                .requestPrimeNumberRange(request, jwt.getSubject())
                .getId();
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRequestId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrimeRequests> getPrimeRequest(
            @PathVariable UUID id,
            @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(primeRequestService.getPrimeRequest(id, jwt.getSubject()));
    }

    @GetMapping
    public ResponseEntity<PageableResult<PrimeRequestResponseDTO>> getAllPrimeRequests(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) int size,
            @AuthenticationPrincipal Jwt jwt) {
        PageableResult<PrimeRequestResponseDTO> response = primeRequestService
                .getAllPrimeRequests(PageableRequest.of(page, size), jwt.getSubject())
                .map(PrimeRequestResponseDTO::from);
        return ResponseEntity.ok(response);
    }
}
