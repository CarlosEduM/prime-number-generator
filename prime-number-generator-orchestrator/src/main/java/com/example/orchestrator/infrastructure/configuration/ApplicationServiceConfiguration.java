package com.example.orchestrator.infrastructure.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.orchestrator.application.services.PrimeRequestService;
import com.example.orchestrator.domain.repositories.PrimeRequestRepository;

@Configuration
public class ApplicationServiceConfiguration {
    @Bean
    PrimeRequestService primeRequestService(PrimeRequestRepository primeRequestRepository) {
        return new PrimeRequestService(primeRequestRepository);
    }
}
