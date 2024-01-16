package org.example.primenumbergenerator.service;

import org.example.primenumbergenerator.service.impl.ViewPrinterServiceImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ViewPrinterServiceTest {
    public static final ByteArrayOutputStream OUTPUT_STREAM = new ByteArrayOutputStream();
    public static final Integer OUTPUT_RESPONSE = 10;
    private static ViewPrinterService viewPrinterService;

    @BeforeAll
    static void beforeAll() {
        byte[] bytesValue = ByteBuffer.allocate(4).putInt(OUTPUT_RESPONSE).array();

        viewPrinterService = new ViewPrinterServiceImpl(OUTPUT_STREAM);
    }

    @AfterAll
    static void afterAll() throws IOException {
        viewPrinterService.close();
    }

    @Test
    void print_happyPath() throws IOException {
        String test = "Test\n{{}}";
        List<Integer> list = IntStream.range(0, 16).boxed().toList();
        viewPrinterService.print(test, list);
        String expected = "Test\n" +
                "  0  1  2  3  4  5  6  7  8  9 10 11 12 13\n" +
                " 14 15";

        assertEquals(expected, OUTPUT_STREAM.toString());
    }

    @Test
    void print_emptyList() throws IOException {
        String test = "Test\n{{}}";
        List<Integer> list = new ArrayList<>();
        viewPrinterService.print(test, list);
        String expected = "Test\n";

        assertEquals(expected, OUTPUT_STREAM.toString());
    }

    @Test
    void print_nullList() {
        assertThrows(NullPointerException.class, () -> viewPrinterService.print("Test", null));
    }
}