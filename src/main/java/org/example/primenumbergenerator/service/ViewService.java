package org.example.primenumbergenerator.service;

import java.io.Closeable;
import java.util.List;

public interface ViewService extends Closeable {
    Integer welcome();

    Integer configurePrimeListWithLengthLimit();

    Long configurePrimeListWithLargestNumberLimit();

    Long configureLargestPrime();

    void response(List<Long> primeList);

    void error(String errorMessage);
}
