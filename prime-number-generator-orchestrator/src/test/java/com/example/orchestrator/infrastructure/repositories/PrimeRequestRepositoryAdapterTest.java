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
        String ownerId = "user-1";
        PrimeRequests expected = new PrimeRequests();
        when(jpaRepository.findByIdAndOwnerId(id, ownerId)).thenReturn(Optional.of(expected));

        PrimeRequests actual = repositoryAdapter.getByIdAndOwnerId(id, ownerId);

        assertSame(expected, actual);
        verify(jpaRepository).findByIdAndOwnerId(id, ownerId);
    }

    @Test
    void testGetByIdThrowsWhenPrimeRequestDoesNotExist() {
        UUID id = UUID.randomUUID();
        String ownerId = "user-1";
        when(jpaRepository.findByIdAndOwnerId(id, ownerId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> repositoryAdapter.getByIdAndOwnerId(id, ownerId));
    }

    @Test
    void testFindAllMapsPageableRequestIncludingSortAndPageMetadata() {
        PrimeRequests primeRequest = new PrimeRequests();
        PageableRequest request = new PageableRequest(
                1,
                5,
                List.of(new PageableRequest.Sort("createdAt", PageableRequest.Direction.DESC)));
        String ownerId = "user-1";
        PageRequest springPageRequest = PageRequest.of(
                1,
                5,
                Sort.by(Sort.Order.desc("createdAt")));
        when(jpaRepository.findAllByOwnerId(ownerId, springPageRequest)).thenReturn(
                new PageImpl<>(List.of(primeRequest), springPageRequest, 12));

        PageableResult<PrimeRequests> actual = repositoryAdapter.findAllByOwnerId(ownerId, request);

        assertEquals(List.of(primeRequest), actual.content());
        assertEquals(1, actual.page());
        assertEquals(5, actual.size());
        assertEquals(12, actual.totalElements());
        verify(jpaRepository).findAllByOwnerId(ownerId, springPageRequest);
    }
}
