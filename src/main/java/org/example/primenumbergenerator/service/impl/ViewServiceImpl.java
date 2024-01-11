package org.example.primenumbergenerator.service.impl;

import org.example.primenumbergenerator.service.ViewService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class ViewServiceImpl implements ViewService {
    private final InputStream inputStream;
    private final OutputStream outputStream;

    public ViewServiceImpl(InputStream inputStream, OutputStream outputStream) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    @Override
    public void print(String input) throws IOException {
        outputStream.write(input.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public Integer readInteger() throws IOException {
        byte[] bytes = inputStream.readAllBytes();
        return ByteBuffer.wrap(bytes).getInt();
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
        outputStream.close();
    }
}
