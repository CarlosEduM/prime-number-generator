package com.example.processor.infrastructure.messaging;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.example.processor.application.dtos.ProcessingResultUpdateDTO;
import com.example.processor.application.messaging.ProcessingResultUpdateProducer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class KafkaProcessingResultUpdateProducer implements ProcessingResultUpdateProducer {
    private final KafkaTemplate<String, ProcessingResultUpdateDTO> kafkaTemplate;
    private final String topic;

    public KafkaProcessingResultUpdateProducer(
            KafkaTemplate<String, ProcessingResultUpdateDTO> kafkaTemplate,
            @Value("${prime-number-generator.kafka.result-topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    @Override
    public void produce(UUID key, ProcessingResultUpdateDTO message) {
        log.info("Producing processing result update to Kafka topic {} with key {}", topic, key);
        log.debug("Message content: {}", message);
        kafkaTemplate.send(topic, key.toString(), message).join();
    }
}
