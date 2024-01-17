package org.example.primenumbergenerator.service;

import org.example.primenumbergenerator.service.impl.ViewPrinterServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ViewPrinterServiceTest {
    public ByteArrayOutputStream outputStream;
    public final Integer OUTPUT_RESPONSE = 10;
    private ViewPrinterService viewPrinterService;

    @BeforeEach
    void setUp() {
        outputStream = new ByteArrayOutputStream();
        ByteBuffer.allocate(4).putInt(OUTPUT_RESPONSE);

        viewPrinterService = new ViewPrinterServiceImpl(outputStream);
    }

    @AfterEach
    void tearDown() throws IOException {
        viewPrinterService.close();
    }

    @Test
    void print_happyPath() {
        String test = "Test\n{{}}";
        List<Integer> list = IntStream.range(0, 16).boxed().toList();
        viewPrinterService.print(test, list);
        String expected = """
                Test
                  0  1  2  3  4  5  6  7  8  9 10 11 12 13
                 14 15""";

        assertEquals(expected, outputStream.toString());
    }

    @Test
    void print_emptyList() {
        String test = "Test\n{{}}";
        List<Integer> emptyList = new ArrayList<>();
        viewPrinterService.print(test, emptyList);
        String expected = "Test\n";

        assertEquals(expected, outputStream.toString());
    }

    @Test
    void print_nullList() {
        assertThrows(NullPointerException.class, () -> viewPrinterService.print("Test", null));
    }
}