package org.example.primenumbergenerator.service;

import org.example.primenumbergenerator.exception.PrimeGeneratorException;
import org.example.primenumbergenerator.service.impl.ReaderServiceImpl;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class ReaderServiceTest {
    @Test
    void readInteger_happyPath() throws IOException {
        Integer outputResponse = 10;
        byte[] bytesValue = ByteBuffer.allocate(4).putInt(outputResponse).array();

        try (ReaderService readerService = new ReaderServiceImpl(new ByteArrayInputStream(bytesValue))) {
            int resp = readerService.readInteger();

            assertEquals(outputResponse, resp);
        }
    }

    @Test
    void readInteger_nonInteger() throws IOException {
        byte[] bytesValue = "abc".getBytes(StandardCharsets.UTF_8);

        try (ReaderService readerService = new ReaderServiceImpl(new ByteArrayInputStream(bytesValue))) {
            assertThrows(PrimeGeneratorException.class, readerService::readInteger);
        }
    }

    @Test
    void waitSomeInput_happyPath() throws IOException {
        byte[] bytesValue = ByteBuffer.allocate(4).putInt(0).array();

        try (ReaderService readerService = new ReaderServiceImpl(new ByteArrayInputStream(bytesValue))) {
            assertDoesNotThrow(readerService::waitSomeInput);
        }
    }

    @Test
    void readLong_happyPath() throws IOException {
        Long outputResponse = 10L;
        byte[] bytesValue = ByteBuffer.allocate(8).putLong(outputResponse).array();

        try (ReaderService readerService = new ReaderServiceImpl(new ByteArrayInputStream(bytesValue))) {
            Long resp = readerService.readLong();

            assertEquals(outputResponse, resp);
        }
    }

    @Test
    void readInteger_nonLong() throws IOException {
        byte[] bytesValue = "abc".getBytes(StandardCharsets.UTF_8);

        try (ReaderService readerService = new ReaderServiceImpl(new ByteArrayInputStream(bytesValue))) {
            assertThrows(PrimeGeneratorException.class, readerService::readLong);
        }
    }
}