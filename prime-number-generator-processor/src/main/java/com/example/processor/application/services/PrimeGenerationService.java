package com.example.processor.application.services;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.jspecify.annotations.NonNull;

import com.example.processor.application.dtos.ProcessingResultUpdateDTO;
import com.example.processor.application.messaging.ProcessingResultUpdateProducer;
import com.example.processor.domain.pojos.PrimeGenerator;

public class PrimeGenerationService {
    private final PrimeGenerator primeGenerator;
    private final ProcessingResultUpdateProducer processingResultUpdateProducer;

    public PrimeGenerationService(
            PrimeGenerator primeGenerator,
            ProcessingResultUpdateProducer processingResultUpdateProducer) {
        this.primeGenerator = primeGenerator;
        this.processingResultUpdateProducer = processingResultUpdateProducer;
    }

    public List<BigInteger> generatePrimes(
            @NonNull UUID processingResultId,
            @NonNull BigInteger inputNumberMin,
            @NonNull BigInteger inputNumberMax) {
        if (processingResultId == null)
            throw new IllegalArgumentException("Processing result ID must not be null.");

        processingResultUpdateProducer.produce(
                processingResultId,
                ProcessingResultUpdateDTO.processing(LocalDateTime.now()));

        List<BigInteger> primes;
        try {
            primes = primeGenerator.generate(inputNumberMin, inputNumberMax);
        } catch (IllegalArgumentException exception) {
            processingResultUpdateProducer.produce(
                    processingResultId,
                    ProcessingResultUpdateDTO.fail(exception.getMessage(), LocalDateTime.now()));
            throw exception;
        }

        processingResultUpdateProducer.produce(
                processingResultId,
                ProcessingResultUpdateDTO.success(primes, LocalDateTime.now()));
        return primes;
    }
}
