package org.example.primenumbergenerator.service;

import org.example.primenumbergenerator.exception.PrimeGeneratorException;
import org.example.primenumbergenerator.service.impl.ReaderServiceImpl;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ReaderServiceTest {
    @Test
    void readInteger_happyPath() throws IOException {
        Integer outputResponse = 10;
        byte[] bytesValue = outputResponse.toString().getBytes(StandardCharsets.UTF_8);

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
        byte[] bytesValue = "19\n\n".getBytes(StandardCharsets.UTF_8);

        try (ReaderService readerService = new ReaderServiceImpl(new ByteArrayInputStream(bytesValue))) {
            assertDoesNotThrow(readerService::waitSomeInput);
        }
    }

    @Test
    void waitSomeInput_happyPathAfterRead() throws IOException {
        byte[] bytesValue = "19\n".getBytes(StandardCharsets.UTF_8);

        try (ReaderService readerService = new ReaderServiceImpl(new ByteArrayInputStream(bytesValue))) {
            assertDoesNotThrow(readerService::readInteger);
            assertDoesNotThrow(readerService::waitSomeInput);
        }
    }

    @Test
    void readLong_happyPath() throws IOException {
        Long outputResponse = 10L;
        byte[] bytesValue = outputResponse.toString().getBytes(StandardCharsets.UTF_8);

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