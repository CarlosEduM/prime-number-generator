package org.example.primenumbergenerator.config;

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
        ClassLoaderService classLoaderService = new ClassLoaderServiceImpl();
        ViewPrinterService viewPrinterService = new ViewPrinterServiceImpl(System.out);
        ViewReaderService viewReaderService = new ViewReaderServiceImpl(System.in);
        return new ViewServiceImpl(viewPrinterService, viewReaderService, classLoaderService);
    }

    @Override
    public void close() throws IOException {
        primeGeneratorController.close();
    }
}
