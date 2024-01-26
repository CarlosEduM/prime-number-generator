package org.example.primenumbergenerator.service;

import java.util.List;

public interface PrimeGeneratorService {
    List<Long> generatePrimeListWithLengthLimit(Integer length);

    List<Long> generatePrimeListWithLargestNumberLimit(Long limit);

    Long generateLargestPrime(Long limit);
}
