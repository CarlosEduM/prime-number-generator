package com.example.orchestrator.infrastructure.messaging;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import com.example.orchestrator.application.dtos.PrimeNumberRangeMessage;

@ExtendWith(MockitoExtension.class)
class KafkaPrimeRequestMessageProducerTest {
    private static final String TOPIC = "prime-number-requests";

    @Mock
    private KafkaTemplate<String, PrimeNumberRangeMessage> kafkaTemplate;

    @Test
    void testProduceSendsRangeAndUuidKeyToConfiguredTopic() {
        KafkaPrimeRequestMessageProducer producer = new KafkaPrimeRequestMessageProducer(kafkaTemplate, TOPIC);
        PrimeNumberRangeMessage message = new PrimeNumberRangeMessage(BigInteger.ZERO, BigInteger.valueOf(100));
        UUID key = UUID.randomUUID();
        when(kafkaTemplate.send(TOPIC, key.toString(), message))
                .thenReturn(CompletableFuture.completedFuture(null));

        producer.produce(key, message);

        verify(kafkaTemplate).send(TOPIC, key.toString(), message);
    }

    @Test
    void testProducePropagatesKafkaSendFailure() {
        KafkaPrimeRequestMessageProducer producer = new KafkaPrimeRequestMessageProducer(kafkaTemplate, TOPIC);
        PrimeNumberRangeMessage message = new PrimeNumberRangeMessage(BigInteger.ZERO, BigInteger.TEN);
        UUID key = UUID.randomUUID();
        when(kafkaTemplate.send(TOPIC, key.toString(), message))
                .thenReturn(CompletableFuture.failedFuture(new IllegalStateException("Kafka unavailable")));

        assertThrows(CompletionException.class, () -> producer.produce(key, message));
    }
}
