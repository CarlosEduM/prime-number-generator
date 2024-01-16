package org.example.primenumbergenerator.service.impl;

import org.example.primenumbergenerator.service.ViewPrinterService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ViewPrinterServiceImpl implements ViewPrinterService {
    public static final Integer MAX_DATA_PER_LINE = 14;
    private final OutputStream outputStream;

    public ViewPrinterServiceImpl(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public void print(String input) throws IOException {
        outputStream.write(input.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void print(String input, List<?> parameters) throws IOException {
        String formattedString = IntStream.range(0, parameters.size())
                .boxed()
                .collect(Collectors.groupingBy(counter -> counter / MAX_DATA_PER_LINE))
                .values()
                .stream()
                .map(e -> "%3d".repeat(e.size()))
                .collect(Collectors.joining("\n"));

        String data = String.format(formattedString, parameters.toArray());


        print(input.replace("{{}}", data));
    }

    @Override
    public void close() throws IOException {
        outputStream.close();
    }
}
