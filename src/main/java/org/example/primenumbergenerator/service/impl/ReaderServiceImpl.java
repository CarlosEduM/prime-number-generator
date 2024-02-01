package org.example.primenumbergenerator.service.impl;

import org.example.primenumbergenerator.exception.PrimeGeneratorException;
import org.example.primenumbergenerator.service.ReaderService;

import java.io.InputStream;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ReaderServiceImpl implements ReaderService {
    private final Scanner scanner;

    public ReaderServiceImpl(InputStream inputStream) {
        this.scanner = new Scanner(inputStream);
    }

    @Override
    public Integer readInteger() {
        try {
            return scanner.nextInt();
        } catch (InputMismatchException e) {
            throw new PrimeGeneratorException("The input is not a integer", e);
        }
    }

    @Override
    public Long readLong() {
        try {
            return scanner.nextLong();
        } catch (InputMismatchException e) {
            throw new PrimeGeneratorException("The input is not a long", e);
        }
    }

    @Override
    public void waitSomeInput() {
        scanner.nextLine();

        if (scanner.hasNextLine())
            scanner.nextLine();
    }

    @Override
    public void close() {
        scanner.close();
    }
}
