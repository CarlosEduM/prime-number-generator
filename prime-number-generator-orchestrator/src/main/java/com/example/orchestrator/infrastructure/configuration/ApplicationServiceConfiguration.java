package com.example.orchestrator.infrastructure.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.orchestrator.application.messaging.PrimeRequestMessageProducer;
import com.example.orchestrator.application.services.PrimeRequestService;
import com.example.orchestrator.application.services.ProcessingResultUpdateService;
import com.example.orchestrator.domain.repositories.PrimeRequestRepository;

@Configuration
public class ApplicationServiceConfiguration {
    @Bean
    PrimeRequestService primeRequestService(
            PrimeRequestRepository primeRequestRepository,
            PrimeRequestMessageProducer primeRequestMessageProducer) {
        return new PrimeRequestService(primeRequestRepository, primeRequestMessageProducer);
    }

    @Bean
    ProcessingResultUpdateService processingResultUpdateService(PrimeRequestRepository primeRequestRepository) {
        return new ProcessingResultUpdateService(primeRequestRepository);
    }
}
