package org.example.primenumbergenerator.controller;

import org.example.primenumbergenerator.service.PrimeGeneratorService;
import org.example.primenumbergenerator.service.ViewService;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PrimeGeneratorControllerTest {
    private static PrimeGeneratorService primeGeneratorService;
    private static ViewService viewService;
    private static PrimeGeneratorController primeGeneratorController;

    @BeforeEach
    void setUp() {
        primeGeneratorService = mock(PrimeGeneratorService.class);
        viewService = mock(ViewService.class);
        primeGeneratorController = new PrimeGeneratorController(primeGeneratorService, viewService);
    }

    @AfterEach
    void tearDown() throws IOException {
        primeGeneratorController.close();
    }

    @Test
    void welcome_happyPath1() {
        List<Long> emptyList = new ArrayList<>();

        when(viewService.welcome()).thenReturn(1);
        when(viewService.configurePrimeListWithLengthLimit()).thenReturn(2);
        when(primeGeneratorService.generatePrimeListWithLengthLimit(2)).thenReturn(emptyList);
        doNothing().when(viewService).response(emptyList);

        Boolean isToContinue = primeGeneratorController.welcome();

        assertTrue(isToContinue);
        verify(viewService, times(1)).welcome();
        verify(viewService, times(1)).configurePrimeListWithLengthLimit();
        verify(viewService, times(1)).response(any());
        verify(primeGeneratorService, times(1)).generatePrimeListWithLengthLimit(any());
    }

    @Test
    void welcome_happyPath2() {
        List<Long> emptyList = new ArrayList<>();

        when(viewService.welcome()).thenReturn(2);
        when(viewService.configurePrimeListWithLargestNumberLimit()).thenReturn(2L);
        when(primeGeneratorService.generatePrimeListWithLargestNumberLimit(2L)).thenReturn(emptyList);
        doNothing().when(viewService).response(emptyList);

        Boolean isToContinue = primeGeneratorController.welcome();

        assertTrue(isToContinue);
        verify(viewService, times(1)).welcome();
        verify(viewService, times(1)).configurePrimeListWithLargestNumberLimit();
        verify(viewService, times(1)).response(any());
        verify(primeGeneratorService, times(1)).generatePrimeListWithLargestNumberLimit(any());
    }

    @Test
    void welcome_happyPath3() {
        List<Long> singletonList = Collections.singletonList(1L);

        when(viewService.welcome()).thenReturn(3);
        when(viewService.configureLargestPrime()).thenReturn(2L);
        when(primeGeneratorService.generateLargestPrime(2L)).thenReturn(1L);
        doNothing().when(viewService).response(singletonList);

        Boolean isToContinue = primeGeneratorController.welcome();

        assertTrue(isToContinue);
        verify(viewService, times(1)).welcome();
        verify(viewService, times(1)).configureLargestPrime();
        verify(viewService, times(1)).response(any());
        verify(primeGeneratorService, times(1)).generateLargestPrime(any());
    }

    @Test
    void welcome_optionWrong() {
        when(viewService.welcome()).thenReturn(4);
        doNothing().when(viewService).error("Option does not exist");

        Boolean isToContinue = primeGeneratorController.welcome();

        assertTrue(isToContinue);
        verify(viewService, times(1)).welcome();
        verify(viewService, times(1)).error(eq("Option does not exist"));
    }

    @Test
    void welcome_exit() {
        when(viewService.welcome()).thenReturn(0);

        Boolean isToContinue = primeGeneratorController.welcome();

        assertFalse(isToContinue);
    }
}