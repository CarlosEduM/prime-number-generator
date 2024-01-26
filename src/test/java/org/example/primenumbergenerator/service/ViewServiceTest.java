package org.example.primenumbergenerator.service;

import org.example.primenumbergenerator.service.impl.ViewServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ViewServiceTest {
    private ViewPrinterService printerService;
    private ViewReaderService readerService;
    private ClassLoaderService classLoaderService;
    private ViewService viewService;

    @BeforeEach
    void setUp() {
        printerService = mock(ViewPrinterService.class);
        readerService = mock(ViewReaderService.class);
        classLoaderService = mock(ClassLoaderService.class);
        viewService = new ViewServiceImpl(printerService, readerService, classLoaderService);
    }

    @Test
    void welcome_happyPath() {
        int expectedValue = 1;
        when(classLoaderService.loadResource("views/welcome.txt")).thenReturn("Test");
        doNothing().when(printerService).print("Test");
        when(readerService.readInteger()).thenReturn(expectedValue);

        int option = viewService.welcome();

        assertEquals(expectedValue, option);
        verify(classLoaderService).loadResource(eq("views/welcome.txt"));
        verify(printerService).print(eq("Test"));
    }

    @Test
    void configurePrimeListWithLengthLimit_happyPath() {
        int expectedValue = 1;
        when(classLoaderService.loadResource("views/configurePrimeListWithLengthLimit.txt"))
                .thenReturn("Test");
        doNothing().when(printerService).print("Test");
        when(readerService.readInteger()).thenReturn(expectedValue);

        long option = viewService.configurePrimeListWithLengthLimit();

        assertEquals(expectedValue, option);
        verify(classLoaderService).loadResource(eq("views/configurePrimeListWithLengthLimit.txt"));
        verify(printerService).print(eq("Test"));
    }

    @Test
    void configurePrimeListWithLargestNumberLimit_happyPath() {
        long expectedValue = 1L;
        when(classLoaderService.loadResource("views/configurePrimeListWithLargestNumberLimit.txt"))
                .thenReturn("Test");
        doNothing().when(printerService).print("Test");
        when(readerService.readLong()).thenReturn(expectedValue);

        long option = viewService.configurePrimeListWithLargestNumberLimit();

        assertEquals(expectedValue, option);
        verify(classLoaderService).loadResource(eq("views/configurePrimeListWithLargestNumberLimit.txt"));
        verify(printerService).print(eq("Test"));
    }

    @Test
    void configureLargestPrime_happyPath() {
        long expectedValue = 1L;
        when(classLoaderService.loadResource("views/configureLargestPrime.txt")).thenReturn("Test");
        doNothing().when(printerService).print("Test");
        when(readerService.readLong()).thenReturn(expectedValue);

        long option = viewService.configureLargestPrime();

        assertEquals(expectedValue, option);
        verify(classLoaderService).loadResource(eq("views/configureLargestPrime.txt"));
        verify(printerService).print(eq("Test"));
    }

    @Test
    void response_happyPath() {
        List<Long> emptyList = new ArrayList<>();
        when(classLoaderService.loadResource("views/response.txt")).thenReturn("Test");
        doNothing().when(printerService).print("Test", emptyList);
        doNothing().when(readerService).waitSomeInput();

        assertDoesNotThrow(() -> viewService.response(emptyList));

        verify(classLoaderService).loadResource(eq("views/response.txt"));
        verify(printerService).print(eq("Test"), eq(emptyList));
    }
}