package com.example.processor.application.messaging;

import java.util.UUID;

import com.example.processor.application.dtos.ProcessingResultUpdateDTO;

public interface ProcessingResultUpdateProducer {
    void produce(UUID key, ProcessingResultUpdateDTO message);
}
