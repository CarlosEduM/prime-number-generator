package com.example.orchestrator.infrastructure.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.example.orchestrator.domain.entities.PrimeRequests;
import com.example.orchestrator.domain.exceptions.NotFoundException;
import com.example.orchestrator.domain.pojos.PageableRequest;
import com.example.orchestrator.domain.pojos.PageableResult;

@ExtendWith(MockitoExtension.class)
class PrimeRequestRepositoryAdapterTest {
    @Mock
    private PrimeRequestJpaRepository jpaRepository;

    private PrimeRequestRepositoryAdapter repositoryAdapter;

    @BeforeEach
    void setUp() {
        repositoryAdapter = new PrimeRequestRepositoryAdapter(jpaRepository);
    }

    @Test
    void testSaveDelegatesToJpaRepository() {
        PrimeRequests primeRequest = new PrimeRequests();
        when(jpaRepository.save(primeRequest)).thenReturn(primeRequest);

        PrimeRequests actual = repositoryAdapter.save(primeRequest);

        assertSame(primeRequest, actual);
        verify(jpaRepository).save(primeRequest);
    }

    @Test
    void testGetByIdReturnsPrimeRequest() {
        UUID id = UUID.randomUUID();
        PrimeRequests expected = new PrimeRequests();
        when(jpaRepository.findById(id)).thenReturn(Optional.of(expected));

        PrimeRequests actual = repositoryAdapter.getById(id);

        assertSame(expected, actual);
    }

    @Test
    void testGetByIdThrowsWhenPrimeRequestDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(jpaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> repositoryAdapter.getById(id));
    }

    @Test
    void testFindAllMapsPageableRequestIncludingSortAndPageMetadata() {
        PrimeRequests primeRequest = new PrimeRequests();
        PageableRequest request = new PageableRequest(
                1,
                5,
                List.of(new PageableRequest.Sort("createdAt", PageableRequest.Direction.DESC)));
        PageRequest springPageRequest = PageRequest.of(
                1,
                5,
                Sort.by(Sort.Order.desc("createdAt")));
        when(jpaRepository.findAll(springPageRequest)).thenReturn(
                new PageImpl<>(List.of(primeRequest), springPageRequest, 12));

        PageableResult<PrimeRequests> actual = repositoryAdapter.findAll(request);

        assertEquals(List.of(primeRequest), actual.content());
        assertEquals(1, actual.page());
        assertEquals(5, actual.size());
        assertEquals(12, actual.totalElements());
        verify(jpaRepository).findAll(springPageRequest);
    }
}
