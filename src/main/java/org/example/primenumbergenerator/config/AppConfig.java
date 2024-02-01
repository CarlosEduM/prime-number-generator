package org.example.primenumbergenerator.config;

import org.example.PrimeNumberGenerator;
import org.example.primenumbergenerator.controller.PrimeGeneratorController;
import org.example.primenumbergenerator.math.PrimeVerifier;
import org.example.primenumbergenerator.service.*;
import org.example.primenumbergenerator.service.impl.*;

import java.io.Closeable;
import java.io.IOException;

public class AppConfig implements Closeable {
    private final PrimeGeneratorController primeGeneratorController;

    public AppConfig() {
        PrimeGeneratorService primeGeneratorService = getPrimeGeneratorService();
        ViewService viewService = getViewService();
        primeGeneratorController = new PrimeGeneratorController(primeGeneratorService, viewService);
    }

    private static PrimeGeneratorService getPrimeGeneratorService() {
        PrimeVerifier primeVerifier = new PrimeVerifier();
        return new PrimeGeneratorServiceImpl(primeVerifier);
    }

    private static ViewService getViewService() {
        ClassLoader classLoader = PrimeNumberGenerator.class.getClassLoader();
        ClassLoaderService classLoaderService = new ClassLoaderServiceImpl(classLoader);
        PrinterService printerService = new PrinterServiceImpl(System.out);
        ReaderService readerService = new ReaderServiceImpl(System.in);
        return new ViewServiceImpl(printerService, readerService, classLoaderService);
    }

    public PrimeGeneratorController getPrimeGeneratorController() {
        return primeGeneratorController;
    }

    @Override
    public void close() throws IOException {
        primeGeneratorController.close();
    }
}
