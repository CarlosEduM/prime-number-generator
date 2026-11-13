package com.example.processor.infrastructure.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.processor.application.messaging.ProcessingResultUpdateProducer;
import com.example.processor.application.services.PrimeGenerationService;
import com.example.processor.domain.pojos.PrimeGenerator;

@Configuration
public class ApplicationServiceConfiguration {
    @Bean
    PrimeGenerator primeGenerator() {
        return new PrimeGenerator();
    }

    @Bean
    PrimeGenerationService primeGenerationService(
            PrimeGenerator primeGenerator,
            ProcessingResultUpdateProducer processingResultUpdateProducer) {
        return new PrimeGenerationService(primeGenerator, processingResultUpdateProducer);
    }
}
