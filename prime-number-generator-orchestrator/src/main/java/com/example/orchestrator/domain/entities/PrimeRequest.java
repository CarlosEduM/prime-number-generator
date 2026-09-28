package com.example.orchestrator.domain.entities;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.jspecify.annotations.NonNull;

import com.example.orchestrator.domain.exceptions.IllegalOperationException;
import com.example.orchestrator.domain.exceptions.InvalidInputException;

import jakarta.annotation.Nonnull;
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
public class PrimeRequest {
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

    @OneToMany(targetEntity = ProcessingResult.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "pr_fk", referencedColumnName = "id")
    private List<ProcessingResult> results;

    public PrimeRequest() {
        this.status = PrimeRequestStatus.PENDING;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void setInputNumberRange(@Nonnull BigInteger inputNumberMin, @Nonnull BigInteger inputNumberMax) {
        if (Objects.nonNull(this.inputNumberMax) && Objects.nonNull(this.inputNumberMin))
            throw new IllegalOperationException("Input number range already set.");

        if (inputNumberMin.compareTo(MINOR_INPUT_NUMBER_MIN) < 0) {
            String errorMessage = String.format("Input number minimum must be greater than or equal to %s.",
                    MINOR_INPUT_NUMBER_MIN.toString());
            throw new InvalidInputException(errorMessage);
        }

        BigInteger difference = inputNumberMax.subtract(inputNumberMin);

        if (difference.compareTo(BigInteger.valueOf(10)) < 0)
            throw new InvalidInputException(
                    "Input number maximum must be at least 10 numbers larger than the minimum.");

        if (difference.compareTo(BigInteger.valueOf(1000)) > 0)
            throw new InvalidInputException(
                    "Input number maximum could not be 1000 numbers larger than minimum.");

        this.inputNumberMin = inputNumberMin;
        this.inputNumberMax = inputNumberMax;
    }

    public void setStatus(@NonNull PrimeRequestStatus newStatus) {
        int comparison = this.status.compareTo(newStatus);
        if (this.status.equals(PrimeRequestStatus.COMPLETED))
            throw new InvalidInputException("Cannot set status of a completed request.");

        if (comparison > 0) {
            String errorMessage = String.format("Cannot set status from %s to %s.", this.status, newStatus);
            throw new InvalidInputException(errorMessage);
        }

        this.status = newStatus;
        this.updatedAt = LocalDateTime.now();
    }
}
