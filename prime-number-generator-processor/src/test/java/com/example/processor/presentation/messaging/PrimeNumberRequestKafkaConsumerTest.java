package com.example.processor.presentation.messaging;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.math.BigInteger;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.processor.application.services.PrimeGenerationService;
import com.example.processor.presentation.dtos.PrimeNumberRangeMessage;

@ExtendWith(MockitoExtension.class)
class PrimeNumberRequestKafkaConsumerTest {
    @Mock
    private PrimeGenerationService primeGenerationService;

    @Test
    void testConsumePassesMessageRangeAndKafkaKeyToService() {
        PrimeNumberRequestKafkaConsumer consumer = new PrimeNumberRequestKafkaConsumer(primeGenerationService);
        PrimeNumberRangeMessage message = new PrimeNumberRangeMessage(BigInteger.ZERO, BigInteger.valueOf(100));
        UUID processingResultId = UUID.fromString("6b3539e4-8c9d-4c0c-8e2c-f5b6959d04af");

        consumer.consume(message, processingResultId.toString());

        verify(primeGenerationService).generatePrimes(
                processingResultId,
                message.inputNumberMin(),
                message.inputNumberMax());
    }

    @Test
    void testConsumeRejectsInvalidKafkaKey() {
        PrimeNumberRequestKafkaConsumer consumer = new PrimeNumberRequestKafkaConsumer(primeGenerationService);

        assertThrows(IllegalArgumentException.class,
                () -> consumer.consume(new PrimeNumberRangeMessage(BigInteger.ZERO, BigInteger.TEN), "invalid-key"));

        verifyNoInteractions(primeGenerationService);
    }
}
