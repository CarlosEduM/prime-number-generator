package org.example.primenumbergenerator.controller;

import org.example.primenumbergenerator.exception.PrimeGeneratorException;
import org.example.primenumbergenerator.service.PrimeGeneratorService;
import org.example.primenumbergenerator.service.ViewService;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class PrimeGeneratorController implements Closeable {
    private final PrimeGeneratorService primeGeneratorService;
    private final ViewService viewService;

    public PrimeGeneratorController(PrimeGeneratorService primeGeneratorService, ViewService viewService) {
        this.primeGeneratorService = primeGeneratorService;
        this.viewService = viewService;
    }

    public Boolean welcome() {
        try {
            return tryToWelcome();
        } catch (PrimeGeneratorException e) {
            error(e.getMessage());
            return true;
        }
    }

    private Boolean tryToWelcome() {
        Integer option = viewService.welcome();

        switch (option) {
            case 0 -> {
                return false;
            }
            case 1 -> configurePrimeListWithLengthLimit();
            case 2 -> configurePrimeListWithLargestNumberLimit();
            case 3 -> configureLargestPrime();
            default -> throw new PrimeGeneratorException("Option does not exist");
        }

        return true;
    }


    private void configurePrimeListWithLengthLimit() {
        Integer lengthLimit = viewService.configurePrimeListWithLengthLimit();
        List<Long> primeList = primeGeneratorService.generatePrimeListWithLengthLimit(lengthLimit);

        response(primeList);
    }

    private void configurePrimeListWithLargestNumberLimit() {
        Long largestNumber = viewService.configurePrimeListWithLargestNumberLimit();
        List<Long> primeList = primeGeneratorService.generatePrimeListWithLargestNumberLimit(largestNumber);

        response(primeList);
    }

    private void configureLargestPrime() {
        Long largestNumber = viewService.configureLargestPrime();
        Long largestPrime = primeGeneratorService.generateLargestPrime(largestNumber);

        response(Collections.singletonList(largestPrime));
    }

    private void response(List<Long> primeList) {
        viewService.response(primeList);
    }

    private void error(String errorMessage) {
        viewService.error(errorMessage);
    }

    @Override
    public void close() throws IOException {
        viewService.close();
    }
}
