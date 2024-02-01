package org.example.primenumbergenerator.service.impl;

import org.example.primenumbergenerator.exception.PrimeGeneratorException;
import org.example.primenumbergenerator.service.ReaderService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

public class ReaderServiceImpl implements ReaderService {
    private final InputStream inputStream;

    public ReaderServiceImpl(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public Integer readInteger() {
        try {
            return readBuffer().getInt();
        } catch (BufferUnderflowException e) {
            throw new PrimeGeneratorException("The input is not a integer", e);
        }
    }

    @Override
    public Long readLong() {
        try {
            return readBuffer().getLong();
        } catch (BufferUnderflowException e) {
            throw new PrimeGeneratorException("The input is not a long", e);
        }
    }

    private ByteBuffer readBuffer() {
        try {
            return tryToReadBuffer();
        } catch (IOException e) {
            throw new PrimeGeneratorException(e);
        }
    }

    private ByteBuffer tryToReadBuffer() throws IOException {
        byte[] bytes = inputStream.readAllBytes();
        return ByteBuffer.wrap(bytes);
    }

    @Override
    public void waitSomeInput() {
        try {
            inputStream.read();
        } catch (IOException e) {
            throw new PrimeGeneratorException(e);
        }
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
    }
}
