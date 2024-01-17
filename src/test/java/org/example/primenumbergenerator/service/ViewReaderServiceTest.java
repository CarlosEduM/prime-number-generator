package org.example.primenumbergenerator.service;

import org.example.primenumbergenerator.exception.PrimeGeneratorException;
import org.example.primenumbergenerator.service.impl.ViewReaderServiceImpl;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class ViewReaderServiceTest {
    @Test
    void readInteger_happyPath() throws IOException {
        Integer outputResponse = 10;
        byte[] bytesValue = ByteBuffer.allocate(4).putInt(outputResponse).array();

        try (ViewReaderService viewReaderService = new ViewReaderServiceImpl(new ByteArrayInputStream(bytesValue))) {
            int resp = viewReaderService.readInteger();

            assertEquals(outputResponse, resp);
        }
    }

    @Test
    void readInteger_nonInteger() throws IOException {
        byte[] bytesValue = "abc".getBytes(StandardCharsets.UTF_8);

        try (ViewReaderService viewReaderService = new ViewReaderServiceImpl(new ByteArrayInputStream(bytesValue))) {
            assertThrows(PrimeGeneratorException.class, viewReaderService::readInteger);
        }
    }

    @Test
    void waitSomeInput_happyPath() throws IOException {
        byte[] bytesValue = ByteBuffer.allocate(4).putInt(0).array();

        try (ViewReaderService viewReaderService = new ViewReaderServiceImpl(new ByteArrayInputStream(bytesValue))) {
            assertDoesNotThrow(viewReaderService::waitSomeInput);
        }
    }

    @Test
    void readLong_happyPath() throws IOException {
        Long outputResponse = 10L;
        byte[] bytesValue = ByteBuffer.allocate(8).putLong(outputResponse).array();

        try (ViewReaderService viewReaderService = new ViewReaderServiceImpl(new ByteArrayInputStream(bytesValue))) {
            Long resp = viewReaderService.readLong();

            assertEquals(outputResponse, resp);
        }
    }

    @Test
    void readInteger_nonLong() throws IOException {
        byte[] bytesValue = "abc".getBytes(StandardCharsets.UTF_8);

        try (ViewReaderService viewReaderService = new ViewReaderServiceImpl(new ByteArrayInputStream(bytesValue))) {
            assertThrows(PrimeGeneratorException.class, viewReaderService::readLong);
        }
    }
}