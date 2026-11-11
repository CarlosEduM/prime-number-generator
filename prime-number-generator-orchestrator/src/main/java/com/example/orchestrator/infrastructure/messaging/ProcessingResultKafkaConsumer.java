package com.example.orchestrator.infrastructure.messaging;

import java.util.UUID;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.example.orchestrator.application.dtos.ProcessingResultUpdateDTO;
import com.example.orchestrator.application.services.ProcessingResultUpdateService;

import lombok.extern.slf4j.Slf4j;

@Slf4j 
@Component
public class ProcessingResultKafkaConsumer {
    private final ProcessingResultUpdateService processingResultUpdateService;

    public ProcessingResultKafkaConsumer(ProcessingResultUpdateService processingResultUpdateService) {
        this.processingResultUpdateService = processingResultUpdateService;
    }

    @KafkaListener(topics = "${prime-number-generator.kafka.result-topic}")
    public void consume(
            @Payload ProcessingResultUpdateDTO updateDTO,
            @Header(KafkaHeaders.RECEIVED_KEY) String key) {
        log.info("Consuming processing result update for key {}", key);
        log.debug("Message content: {}", updateDTO);
        processingResultUpdateService.updateProcessingResult(UUID.fromString(key), updateDTO);
    }
}
