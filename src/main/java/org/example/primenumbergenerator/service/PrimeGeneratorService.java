package org.example.primenumbergenerator.service;

import java.util.List;

public interface PrimeGeneratorService {
    List<Long> generateAPrimeList(Integer length);

    List<Long> generateAPrimeListUpTo(Long limit);

    Long generateTheLargestPrimeNumberUpTo(Long limit);
}
