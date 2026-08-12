package com.example.orchestrator.domain.entities;

import java.math.BigInteger;
import java.time.LocalDateTime;

import com.example.orchestrator.domain.exceptions.BadInputException;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class PrimeRequests {
    private static final BigInteger MINOR_INPUT_NUMBER_MIN = BigInteger.ZERO;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private BigInteger inputNumberMin;
    private BigInteger inputNumberMax;
    private PrimeRequestStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
