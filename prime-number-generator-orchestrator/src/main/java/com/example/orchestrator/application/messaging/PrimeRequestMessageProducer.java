package com.example.orchestrator.application.messaging;

import java.util.UUID;

import com.example.orchestrator.application.dtos.PrimeNumberRangeMessage;

public interface PrimeRequestMessageProducer {
    void produce(UUID key, PrimeNumberRangeMessage message);
}
