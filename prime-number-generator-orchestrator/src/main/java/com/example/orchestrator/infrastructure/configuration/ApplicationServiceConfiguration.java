package com.example.orchestrator.infrastructure.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.orchestrator.application.messaging.PrimeRequestMessageProducer;
import com.example.orchestrator.application.services.PrimeRequestService;
import com.example.orchestrator.application.services.ProcessingResultService;
import com.example.orchestrator.domain.repositories.PrimeRequestRepository;
import com.example.orchestrator.domain.repositories.ProcessingResultRepository;

@Configuration
public class ApplicationServiceConfiguration {
    @Bean
    PrimeRequestService primeRequestService(
            PrimeRequestRepository primeRequestRepository,
            PrimeRequestMessageProducer primeRequestMessageProducer) {
        return new PrimeRequestService(primeRequestRepository, primeRequestMessageProducer);
    }

    @Bean
    ProcessingResultService processingResultService(ProcessingResultRepository processingResultRepository) {
        return new ProcessingResultService(processingResultRepository);
    }
}
