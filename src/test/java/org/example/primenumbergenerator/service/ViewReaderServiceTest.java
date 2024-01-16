package org.example.primenumbergenerator.service;

import org.example.primenumbergenerator.service.impl.ViewPrinterServiceImpl;
import org.example.primenumbergenerator.service.impl.ViewReaderServiceImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ViewReaderServiceTest {
    public static final Integer OUTPUT_RESPONSE = 10;
    private static ViewReaderService viewReaderService;

    @BeforeAll
    static void beforeAll() {
        byte[] bytesValue = ByteBuffer.allocate(4).putInt(OUTPUT_RESPONSE).array();

        viewReaderService = new ViewReaderServiceImpl(new ByteArrayInputStream(bytesValue));
    }

    @AfterAll
    static void afterAll() throws IOException {
        viewReaderService.close();
    }

    @Test
    void readInteger() throws IOException {
        Integer resp = viewReaderService.readInteger();

        assertEquals(OUTPUT_RESPONSE, resp);
    }
}