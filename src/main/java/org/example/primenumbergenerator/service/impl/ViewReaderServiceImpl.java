package org.example.primenumbergenerator.service.impl;

import org.example.primenumbergenerator.service.ViewPrinterService;
import org.example.primenumbergenerator.service.ViewReaderService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ViewReaderServiceImpl implements ViewReaderService {
    private final InputStream inputStream;

    public ViewReaderServiceImpl(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public Integer readInteger() throws IOException {
        byte[] bytes = inputStream.readAllBytes();
        return ByteBuffer.wrap(bytes).getInt();
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
    }
}
