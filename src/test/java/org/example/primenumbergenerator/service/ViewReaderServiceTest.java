package org.example.primenumbergenerator.service;

import org.example.primenumbergenerator.service.impl.ViewReaderServiceImpl;
import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ViewReaderServiceTest {
    public static final Integer OUTPUT_RESPONSE = 10;
    private static ViewReaderService viewReaderService;

    @BeforeEach
    void setUp() {
        byte[] bytesValue = ByteBuffer.allocate(4).putInt(OUTPUT_RESPONSE).array();

        viewReaderService = new ViewReaderServiceImpl(new ByteArrayInputStream(bytesValue));
    }

    @AfterEach
    void tearDown() throws IOException {
        viewReaderService.close();
    }

    @Test
    void readInteger() {
        Integer resp = viewReaderService.readInteger();

        assertEquals(OUTPUT_RESPONSE, resp);
    }

    @Test
    void waitSomeInput() {
        assertDoesNotThrow(() -> viewReaderService.waitSomeInput());
    }
}