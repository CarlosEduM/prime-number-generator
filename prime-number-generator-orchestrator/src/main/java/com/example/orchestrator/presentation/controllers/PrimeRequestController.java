package com.example.orchestrator.presentation.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.orchestrator.application.dtos.PrimeRequestDTO;
import com.example.orchestrator.application.services.PrimeRequestService;
import com.example.orchestrator.presentation.dtos.PrimeRequestResponseDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/prime-requests")
public class PrimeRequestController {
    private final PrimeRequestService primeRequestService;

    public PrimeRequestController(PrimeRequestService primeRequestService) {
        this.primeRequestService = primeRequestService;
    }

    @PostMapping
    public ResponseEntity<PrimeRequestResponseDTO> createPrimeRequest(
            @Valid @RequestBody PrimeRequestDTO request,
            @AuthenticationPrincipal Jwt jwt) {
        PrimeRequestResponseDTO response = PrimeRequestResponseDTO.from(
                primeRequestService.requestPrimeNumberRange(request, jwt.getSubject()));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
