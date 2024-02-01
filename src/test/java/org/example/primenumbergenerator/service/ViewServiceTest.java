package org.example.primenumbergenerator.service;

import org.example.primenumbergenerator.service.impl.ViewServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ViewServiceTest {
    private PrinterService printerService;
    private ReaderService readerService;
    private ClassLoaderService classLoaderService;
    private ViewService viewService;

    @BeforeEach
    void setUp() {
        printerService = mock(PrinterService.class);
        readerService = mock(ReaderService.class);
        classLoaderService = mock(ClassLoaderService.class);
        viewService = new ViewServiceImpl(printerService, readerService, classLoaderService);
    }

    @AfterEach
    void tearDown() throws IOException {
        viewService.close();
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

    @Test
    void error_happyPath() {
        when(classLoaderService.loadResource("views/error.txt")).thenReturn("Test");
        doNothing().when(printerService).print("Test", "test");
        doNothing().when(readerService).waitSomeInput();

        assertDoesNotThrow(() -> viewService.error("test"));

        verify(classLoaderService).loadResource(eq("views/error.txt"));
        verify(printerService, times(1)).print("Test", "test");
    }
}