package com.example.orchestrator.application.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.orchestrator.application.dtos.PrimeRequestDTO;
import com.example.orchestrator.domain.entities.PrimeRequests;
import com.example.orchestrator.domain.exceptions.InvalidInputException;
import com.example.orchestrator.domain.pojos.PageableRequest;
import com.example.orchestrator.domain.pojos.PageableResult;
import com.example.orchestrator.domain.repositories.PrimeRequestRepository;

@ExtendWith(MockitoExtension.class)
class PrimeRequestServiceTest {
    @Mock
    private PrimeRequestRepository repository;

    private PrimeRequestService service;

    @BeforeEach
    void setUp() {
        service = new PrimeRequestService(repository);
    }

    @Test
    void testRequestPrimeNumberRangeSavesAndReturnsRequest() {
        PrimeRequestDTO request = new PrimeRequestDTO(BigInteger.ZERO, BigInteger.TEN);
        String ownerId = "user-1";
        PrimeRequests savedRequest = new PrimeRequests();
        when(repository.save(any(PrimeRequests.class))).thenReturn(savedRequest);

        PrimeRequests actual = service.requestPrimeNumberRange(request, ownerId);

        ArgumentCaptor<PrimeRequests> savedRequestCaptor = ArgumentCaptor.forClass(PrimeRequests.class);
        verify(repository).save(savedRequestCaptor.capture());
        assertSame(savedRequest, actual);
        assertEquals(BigInteger.ZERO, savedRequestCaptor.getValue().getInputNumberMin());
        assertEquals(BigInteger.TEN, savedRequestCaptor.getValue().getInputNumberMax());
        assertEquals(ownerId, savedRequestCaptor.getValue().getOwnerId());
    }

    @Test
    void testRequestPrimeNumberRangeRejectsBlankOwnerId() {
        PrimeRequestDTO request = new PrimeRequestDTO(BigInteger.ZERO, BigInteger.TEN);

        assertThrows(InvalidInputException.class, () -> service.requestPrimeNumberRange(request, " "));
    }

    @Test
    void testGetPrimeRequestReturnsRequestForId() {
        UUID id = UUID.randomUUID();
        String ownerId = "user-1";
        PrimeRequests expected = new PrimeRequests();
        when(repository.getByIdAndOwnerId(id, ownerId)).thenReturn(expected);

        PrimeRequests actual = service.getPrimeRequest(id, ownerId);

        assertSame(expected, actual);
        verify(repository).getByIdAndOwnerId(id, ownerId);
    }

    @Test
    void testGetAllPrimeRequestsReturnsPageForRequest() {
        PageableRequest pageableRequest = PageableRequest.of(1, 10);
        String ownerId = "user-1";
        PageableResult<PrimeRequests> expected = new PageableResult<>(List.of(new PrimeRequests()), 1, 10, 1);
        when(repository.findAllByOwnerId(ownerId, pageableRequest)).thenReturn(expected);

        PageableResult<PrimeRequests> actual = service.getAllPrimeRequests(pageableRequest, ownerId);

        assertSame(expected, actual);
        verify(repository).findAllByOwnerId(ownerId, pageableRequest);
    }
}
