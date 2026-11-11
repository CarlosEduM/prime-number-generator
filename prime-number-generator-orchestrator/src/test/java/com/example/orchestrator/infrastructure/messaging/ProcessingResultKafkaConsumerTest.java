package com.example.orchestrator.infrastructure.messaging;

import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.orchestrator.application.dtos.ProcessingResultUpdateDTO;
import com.example.orchestrator.application.services.ProcessingResultUpdateService;
import com.example.orchestrator.domain.entities.ProcessingResultState;

@ExtendWith(MockitoExtension.class)
class ProcessingResultKafkaConsumerTest {
    @Mock
    private ProcessingResultUpdateService processingResultUpdateService;

    @Test
    void testConsumeForwardsPayloadAndKafkaKeyToProcessingResultUpdateService() {
        ProcessingResultKafkaConsumer consumer = new ProcessingResultKafkaConsumer(processingResultUpdateService);
        UUID processingResultId = UUID.randomUUID();
        ProcessingResultUpdateDTO updateDTO = new ProcessingResultUpdateDTO(
                ProcessingResultState.PROCESSING,
                LocalDateTime.of(2026, 11, 11, 12, 0),
                null,
                null,
                null);

        consumer.consume(updateDTO, processingResultId.toString());

        verify(processingResultUpdateService).updateProcessingResult(processingResultId, updateDTO);
    }
}
