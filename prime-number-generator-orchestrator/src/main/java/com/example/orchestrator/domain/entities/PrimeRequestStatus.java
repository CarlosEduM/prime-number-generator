package com.example.orchestrator.domain.entities;

/**
 * PrimeRequestStatus
 * 
 * The request status of a prime number generation request.
 */
public enum PrimeRequestStatus {
    PENDING,
    PROCESSING,
    COMPLETED;

    public PrimeRequestStatus getNext() {
        if (this == PrimeRequestStatus.COMPLETED) {
            throw new IllegalStateException("The request is already completed.");
        }
        int ordinal = this.ordinal();
        return values()[ordinal + 1];
    }
}
