package com.example.orchestrator.domain.entities;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.example.orchestrator.domain.exceptions.BadInputException;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.Getter;

@Entity
@Getter
public class PrimeRequests {
    private static final BigInteger MINOR_INPUT_NUMBER_MIN = BigInteger.ZERO;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private BigInteger inputNumberMin;
    private BigInteger inputNumberMax;
    @Enumerated(EnumType.STRING)
    private PrimeRequestStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    @OneToMany(targetEntity = ProcessingResults.class, cascade = CascadeType.ALL)
    @JoinColumn(name="cp_fk", referencedColumnName = "id")
    private List<ProcessingResults> results;

    public PrimeRequests() {
        this.status = PrimeRequestStatus.PENDING;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void setInputNumberRange(BigInteger inputNumberMin, BigInteger inputNumberMax) {
        if (MINOR_INPUT_NUMBER_MIN.compareTo(inputNumberMin) < 0)
            throw new BadInputException("Input number min must be greater than or equal to zero.");
        
        if (inputNumberMin.compareTo(inputNumberMax.subtract(BigInteger.valueOf(10))) < 0)
            throw new BadInputException(
                "Input number minimum must be at least 10 numbers larger than the maximum number.");

        if (inputNumberMax.compareTo(inputNumberMin.subtract(BigInteger.valueOf(1001))) < 0)
            throw new BadInputException(
                "Input number maximum could not be 1000 numbers larger than minimum.");

        this.inputNumberMin = inputNumberMin;
        this.inputNumberMax = inputNumberMax;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateToNextStatus() {
        this.status = this.status.getNext();
        this.updatedAt = LocalDateTime.now();
    }
}
