package org.example.primenumbergenerator.service;

import org.example.primenumbergenerator.exception.PrimeGeneratorException;
import org.example.primenumbergenerator.service.impl.PrinterServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

class PrinterServiceTest {
    public ByteArrayOutputStream outputStream;
    public final Integer OUTPUT_RESPONSE = 10;
    private PrinterService printerService;

    @BeforeEach
    void setUp() {
        outputStream = new ByteArrayOutputStream();
        ByteBuffer.allocate(4).putInt(OUTPUT_RESPONSE);

        printerService = new PrinterServiceImpl(outputStream);
    }

    @AfterEach
    void tearDown() throws IOException {
        printerService.close();
    }

    @Test
    void print_happyPath() {
        String test = "Test\n{{}}";
        List<Integer> list = IntStream.range(0, 4).boxed().toList();
        printerService.print(test, list);
        StringBuilder builder = new StringBuilder();
        builder.append("Test\n");
        builder.append("0\n");
        builder.append("1\n");
        builder.append("2\n");
        builder.append("3");

        assertEquals(builder.toString(), outputStream.toString());
    }

    @Test
    void print_emptyList() {
        String test = "Test\n{{}}";
        List<Integer> emptyList = new ArrayList<>();
        printerService.print(test, emptyList);
        String expected = "Test\n";

        assertEquals(expected, outputStream.toString());
    }

    @Test
    void print_nullList() {
        assertThrows(NullPointerException.class, () -> printerService.print("Test", (List<?>) null));
    }

    @Test
    void print_Exception() throws IOException {
        OutputStream outputStreamMocked = mock(OutputStream.class);
        PrinterServiceImpl printerService = new PrinterServiceImpl(outputStreamMocked);

        doThrow(IOException.class).when(outputStreamMocked).write(any());

        assertThrows(PrimeGeneratorException.class, () -> printerService.print(""));
    }
}