package com.example.orchestrator.domain.entities;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class ProcessingResults {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long requestId;
    private String processorName;
    private ProcessingResultState state;
    private List<BigInteger> result;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
}
