package org.example.primenumbergenerator.exception;

public class PrimeGeneratorException extends RuntimeException {
    public PrimeGeneratorException(String message) {
        super(message);
    }

    public PrimeGeneratorException(Throwable cause) {
        super(cause);
    }
}
