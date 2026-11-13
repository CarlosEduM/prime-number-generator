package com.example.processor.presentation.messaging;

import java.util.UUID;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.example.processor.application.services.PrimeGenerationService;
import com.example.processor.presentation.dtos.PrimeNumberRangeMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PrimeNumberRequestKafkaConsumer {
    private final PrimeGenerationService primeGenerationService;

    public PrimeNumberRequestKafkaConsumer(PrimeGenerationService primeGenerationService) {
        this.primeGenerationService = primeGenerationService;
    }

    @KafkaListener(topics = "${prime-number-generator.kafka.request-topic}")
    public void consume(
            @Payload PrimeNumberRangeMessage rangeMessage,
            @Header(KafkaHeaders.RECEIVED_KEY) String key) {
        UUID processingResultId = UUID.fromString(key);
        log.info("Consuming prime number range for processing result {}", processingResultId);
        log.debug("Message content: {}", rangeMessage);
        primeGenerationService.generatePrimes(
                processingResultId,
                rangeMessage.inputNumberMin(),
                rangeMessage.inputNumberMax());
    }
}
