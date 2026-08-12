package com.example.orchestrator.domain.exceptions;

public class BadInputException extends RuntimeException {
    public BadInputException(String message) {
        super(message);
    }
}
