package com.example.orchestrator.infrastructure.messaging;

import java.util.UUID;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.example.orchestrator.application.dtos.ProcessingResultUpdateDTO;
import com.example.orchestrator.application.services.ProcessingResultService;

@Component
public class ProcessingResultKafkaConsumer {
    private final ProcessingResultService processingResultService;

    public ProcessingResultKafkaConsumer(ProcessingResultService processingResultService) {
        this.processingResultService = processingResultService;
    }

    @KafkaListener(topics = "${prime-number-generator.kafka.result-topic}")
    public void consume(
            @Payload ProcessingResultUpdateDTO updateDTO,
            @Header(KafkaHeaders.RECEIVED_KEY) String key) {
        processingResultService.updateProcessingResult(UUID.fromString(key), updateDTO);
    }
}
