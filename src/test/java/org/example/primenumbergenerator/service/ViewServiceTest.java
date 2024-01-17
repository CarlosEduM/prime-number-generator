package org.example.primenumbergenerator.service;

import org.example.primenumbergenerator.service.impl.ViewServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ViewServiceTest {
    private static ViewPrinterService printerService;
    private static ViewReaderService readerService;
    private static ClassLoaderService classLoaderService;
    private static ViewService viewService;

    @BeforeAll
    static void beforeAll() {
        printerService = mock(ViewPrinterService.class);
        readerService = mock(ViewReaderService.class);
        classLoaderService = mock(ClassLoaderService.class);
        viewService = new ViewServiceImpl(printerService, readerService, classLoaderService);
    }

    @Test
    void welcome() {
        int expectedValue = 1;
        when(classLoaderService.loadResource("views/welcome.txt")).thenReturn("Test");
        doNothing().when(printerService).print("Test");
        when(readerService.readInteger()).thenReturn(expectedValue);

        Integer option = viewService.welcome();

        assertEquals(expectedValue, option);
        verify(classLoaderService).loadResource(eq("views/welcome.txt"));
        verify(printerService).print(eq("Test"));
    }

    @Test
    void response() {
        List<Integer> emptyList = new ArrayList<>();
        when(classLoaderService.loadResource("views/response.txt")).thenReturn("Test");
        doNothing().when(printerService).print("Test", emptyList);
        doNothing().when(readerService).waitSomeInput();

        assertDoesNotThrow(() -> viewService.response(emptyList));

        verify(classLoaderService).loadResource(eq("views/response.txt"));
        verify(printerService).print(eq("Test"), eq(emptyList));
    }
}