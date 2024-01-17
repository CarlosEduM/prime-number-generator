package org.example.primenumbergenerator.service.impl;

import org.example.primenumbergenerator.exception.PrimeGeneratorException;
import org.example.primenumbergenerator.service.ViewReaderService;

import java.io.IOException;
import java.io.InputStream;
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
        }
    }

    private int tryToReadInteger() throws IOException {
        byte[] bytes = inputStream.readAllBytes();
        return ByteBuffer.wrap(bytes).getInt();
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
