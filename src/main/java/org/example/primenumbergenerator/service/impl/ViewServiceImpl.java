package org.example.primenumbergenerator.service.impl;

import org.example.PrimeNumberGenerator;
import org.example.primenumbergenerator.exception.PrimeGeneratorException;
import org.example.primenumbergenerator.service.ViewPrinterService;
import org.example.primenumbergenerator.service.ViewReaderService;
import org.example.primenumbergenerator.service.ViewService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

public class ViewServiceImpl implements ViewService {
    private final ViewPrinterService viewPrinterService;
    private final ViewReaderService viewReaderService;

    public ViewServiceImpl(ViewPrinterService viewPrinterService, ViewReaderService viewReaderService) {
        this.viewPrinterService = viewPrinterService;
        this.viewReaderService = viewReaderService;
    }


    @Override
    public Integer welcome() {
        printViewFrom("views/welcome.txt");
        return viewReaderService.readInteger();
    }

    private void printViewFrom(String resourcePath) {
        ClassLoader loader = PrimeNumberGenerator.class.getClassLoader();

        try (InputStream resource = loader.getResourceAsStream(resourcePath)) {
            if (resource == null)
                throw new PrimeGeneratorException("Some problem when trying to load '" + resourcePath + "'");

            try (Scanner scanner = new Scanner(resource, StandardCharsets.UTF_8)) {
                while (scanner.hasNextLine())
                    viewPrinterService.print(scanner.nextLine());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void response(List<Integer> primeList) {

    }

    @Override
    public void close() throws IOException {

    }
}
