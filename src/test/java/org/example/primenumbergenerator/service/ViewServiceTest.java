package org.example.primenumbergenerator.service;

import org.example.primenumbergenerator.service.impl.ViewServiceImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ViewServiceTest {
    public static final ByteArrayOutputStream OUTPUT_STREAM = new ByteArrayOutputStream();
    public static final Integer OUTPUT_RESPONSE = 10;
    private static ViewService viewService;

    @BeforeAll
    static void beforeAll() {
        byte[] bytesValue = ByteBuffer.allocate(4).putInt(OUTPUT_RESPONSE).array();

        viewService = new ViewServiceImpl(new ByteArrayInputStream(bytesValue), OUTPUT_STREAM);
    }

    @AfterAll
    static void afterAll() throws IOException {
        viewService.close();
    }

    @Test
    void print() throws IOException {
        String test = "Test";
        viewService.print(test);

        assertEquals(test, OUTPUT_STREAM.toString());
    }

    @Test
    void readInteger() throws IOException {
        Integer resp = viewService.readInteger();

        assertEquals(OUTPUT_RESPONSE, resp);
    }
}