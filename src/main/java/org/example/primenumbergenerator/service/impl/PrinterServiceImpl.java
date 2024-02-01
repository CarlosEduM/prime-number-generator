package org.example.primenumbergenerator.service.impl;

import org.example.primenumbergenerator.exception.PrimeGeneratorException;
import org.example.primenumbergenerator.service.PrinterService;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PrinterServiceImpl implements PrinterService {
    public static final Integer MAX_DATA_PER_LINE = 14;
    private final OutputStream outputStream;

    public PrinterServiceImpl(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public void print(String input) {
        try {
            outputStream.write(input.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new PrimeGeneratorException(e);
        }
    }

    @Override
    public void print(String input, List<?> parameters) {
        String formattedString = IntStream.range(0, parameters.size())
                .boxed()
                .collect(Collectors.groupingBy(counter -> counter / MAX_DATA_PER_LINE))
                .values()
                .stream()
                .map(e -> "%3d".repeat(e.size()))
                .collect(Collectors.joining("\n"));

        String data = String.format(formattedString, parameters.toArray());


        print(input, data);
    }

    @Override
    public void print(String input, String message) {
        print(input.replace("{{}}", message));
    }

    @Override
    public void close() throws IOException {
        outputStream.close();
    }
}
