package com.example.orchestrator.infrastructure.messaging;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.example.orchestrator.application.dtos.PrimeNumberRangeMessage;
import com.example.orchestrator.application.messaging.PrimeRequestMessageProducer;

@Component
public class KafkaPrimeRequestMessageProducer implements PrimeRequestMessageProducer {
    private final KafkaTemplate<String, PrimeNumberRangeMessage> kafkaTemplate;
    private final String topic;

    public KafkaPrimeRequestMessageProducer(
            KafkaTemplate<String, PrimeNumberRangeMessage> kafkaTemplate,
            @Value("${prime-number-generator.kafka.request-topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    @Override
    public void produce(UUID key, PrimeNumberRangeMessage message) {
        kafkaTemplate.send(topic, key.toString(), message).join();
    }
}
