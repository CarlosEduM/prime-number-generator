package org.example.primenumbergenerator.service.impl;

import org.example.primenumbergenerator.exception.PrimeGeneratorException;
import org.example.primenumbergenerator.service.ViewReaderService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

public class ViewReaderServiceImpl implements ViewReaderService {
    private final InputStream inputStream;

    public ViewReaderServiceImpl(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public Integer readInteger() {
        try {
            return tryToReadInteger();
        } catch (IOException e) {
            throw new PrimeGeneratorException(e);
        } catch (BufferUnderflowException e) {
            throw new PrimeGeneratorException("The input is not a integer", e);
        }
    }

    private Integer tryToReadInteger() throws IOException {
        return getByteBuffer().getInt();
    }

    @Override
    public Long readLong() {
        try {
            return tryToReadLong();
        } catch (IOException e) {
            throw new PrimeGeneratorException(e);
        } catch (BufferUnderflowException e) {
            throw new PrimeGeneratorException("The input is not a long", e);
        }
    }

    private Long tryToReadLong() throws IOException {
        return getByteBuffer().getLong();
    }

    private ByteBuffer getByteBuffer() throws IOException {
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
