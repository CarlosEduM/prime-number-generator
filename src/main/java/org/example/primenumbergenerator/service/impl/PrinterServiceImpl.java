package org.example.primenumbergenerator.service.impl;

import org.example.primenumbergenerator.exception.PrimeGeneratorException;
import org.example.primenumbergenerator.service.PrinterService;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PrinterServiceImpl implements PrinterService {
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
    public void print(String input, List<?> parameterList) {
        List<String> formattedParameters = parameterList.stream()
                .map(this::formatParameter)
                .toList();
        Integer parameterListLength = getDataLength(parameterList);

        String formattedString = IntStream.range(0, parameterList.size())
                .mapToObj(index -> "%" + parameterListLength + "s")
                .collect(Collectors.joining("\n"));

        String data = String.format(formattedString, formattedParameters.toArray());

        print(input, data);
    }

    private Integer getDataLength(List<?> parameters) {
        if (parameters.isEmpty())
            return 1;
        else {
            return parameters.get(parameters.size() - 1).toString().length();
        }
    }

    private String formatParameter(Object parameter) {
        if (parameter instanceof Number)
            return NumberFormat.getInstance().format(parameter);

        return parameter.toString();
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
