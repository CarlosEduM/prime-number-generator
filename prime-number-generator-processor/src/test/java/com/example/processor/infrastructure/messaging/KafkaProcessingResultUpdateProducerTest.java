package com.example.processor.infrastructure.messaging;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import com.example.processor.application.dtos.ProcessingResultUpdateDTO;

@ExtendWith(MockitoExtension.class)
class KafkaProcessingResultUpdateProducerTest {
    private static final String TOPIC = "prime-number-results";

    @Mock
    private KafkaTemplate<String, ProcessingResultUpdateDTO> kafkaTemplate;

    @Test
    void testProduceSendsUpdateAndUuidKeyToConfiguredTopic() {
        KafkaProcessingResultUpdateProducer producer = new KafkaProcessingResultUpdateProducer(kafkaTemplate, TOPIC);
        ProcessingResultUpdateDTO message = ProcessingResultUpdateDTO.success(
                List.of(BigInteger.TWO, BigInteger.valueOf(3)),
                LocalDateTime.parse("2026-11-13T10:00:00"));
        UUID key = UUID.fromString("6b3539e4-8c9d-4c0c-8e2c-f5b6959d04af");
        when(kafkaTemplate.send(TOPIC, key.toString(), message))
                .thenReturn(CompletableFuture.completedFuture(null));

        producer.produce(key, message);

        verify(kafkaTemplate).send(TOPIC, key.toString(), message);
    }

    @Test
    void testProducePropagatesKafkaSendFailure() {
        KafkaProcessingResultUpdateProducer producer = new KafkaProcessingResultUpdateProducer(kafkaTemplate, TOPIC);
        ProcessingResultUpdateDTO message = ProcessingResultUpdateDTO.processing(
                LocalDateTime.parse("2026-11-13T10:00:00"));
        UUID key = UUID.fromString("6b3539e4-8c9d-4c0c-8e2c-f5b6959d04af");
        when(kafkaTemplate.send(TOPIC, key.toString(), message))
                .thenReturn(CompletableFuture.failedFuture(new IllegalStateException("Kafka unavailable")));

        assertThrows(CompletionException.class, () -> producer.produce(key, message));
    }
}
