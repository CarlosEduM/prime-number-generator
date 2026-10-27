package com.example.orchestrator.application.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
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
        PrimeRequests savedRequest = new PrimeRequests();
        when(repository.save(any(PrimeRequests.class))).thenReturn(savedRequest);

        PrimeRequests actual = service.requestPrimeNumberRange(request);

        ArgumentCaptor<PrimeRequests> savedRequestCaptor = ArgumentCaptor.forClass(PrimeRequests.class);
        verify(repository).save(savedRequestCaptor.capture());
        assertSame(savedRequest, actual);
        assertEquals(BigInteger.ZERO, savedRequestCaptor.getValue().getInputNumberMin());
        assertEquals(BigInteger.TEN, savedRequestCaptor.getValue().getInputNumberMax());
    }

    @Test
    void testGetPrimeRequestReturnsRequestForId() {
        UUID id = UUID.randomUUID();
        PrimeRequests expected = new PrimeRequests();
        when(repository.getById(id)).thenReturn(expected);

        PrimeRequests actual = service.getPrimeRequest(id);

        assertSame(expected, actual);
        verify(repository).getById(id);
    }

    @Test
    void testGetAllPrimeRequestsReturnsPageForRequest() {
        PageableRequest pageableRequest = PageableRequest.of(1, 10);
        PageableResult<PrimeRequests> expected = new PageableResult<>(List.of(new PrimeRequests()), 1, 10, 1);
        when(repository.findAll(pageableRequest)).thenReturn(expected);

        PageableResult<PrimeRequests> actual = service.getAllPrimeRequests(pageableRequest);

        assertSame(expected, actual);
        verify(repository).findAll(pageableRequest);
    }
}
