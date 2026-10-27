package com.example.orchestrator.infrastructure.repositories;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.orchestrator.domain.entities.ProcessingResults;
import com.example.orchestrator.domain.exceptions.NotFoundException;

@ExtendWith(MockitoExtension.class)
class ProcessingResultRepositoryAdapterTest {
    @Mock
    private ProcessingResultsJpaRepository jpaRepository;

    private ProcessingResultRepositoryAdapter repositoryAdapter;

    @BeforeEach
    void setUp() {
        repositoryAdapter = new ProcessingResultRepositoryAdapter(jpaRepository);
    }

    @Test
    void testGetByIdReturnsProcessingResult() {
        UUID id = UUID.randomUUID();
        ProcessingResults expected = new ProcessingResults();
        when(jpaRepository.findById(id)).thenReturn(Optional.of(expected));

        ProcessingResults actual = repositoryAdapter.getById(id);

        assertSame(expected, actual);
    }

    @Test
    void testGetByIdThrowsWhenProcessingResultDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(jpaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> repositoryAdapter.getById(id));
    }

    @Test
    void testSaveDelegatesToJpaRepository() {
        ProcessingResults processingResults = new ProcessingResults();
        when(jpaRepository.save(processingResults)).thenReturn(processingResults);

        ProcessingResults actual = repositoryAdapter.save(processingResults);

        assertSame(processingResults, actual);
        verify(jpaRepository).save(processingResults);
    }
}
