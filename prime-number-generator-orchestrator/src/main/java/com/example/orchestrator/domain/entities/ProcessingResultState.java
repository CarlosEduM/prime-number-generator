package com.example.orchestrator.domain.entities;

/**
 * ProcessingResultStatus
 * 
 * The actual state of the processing result.
 */
public enum ProcessingResultState {
    IN_QUEUE,
    PROCESSING,
    SUCCESS,
    FAIL
}
