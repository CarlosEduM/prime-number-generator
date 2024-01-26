package org.example.primenumbergenerator.service.impl;

import org.example.primenumbergenerator.service.ClassLoaderService;
import org.example.primenumbergenerator.service.ViewPrinterService;
import org.example.primenumbergenerator.service.ViewReaderService;
import org.example.primenumbergenerator.service.ViewService;

import java.io.IOException;
import java.util.List;

public class ViewServiceImpl implements ViewService {
    private final ViewPrinterService printerService;
    private final ViewReaderService readerService;
    private final ClassLoaderService classLoaderService;

    public ViewServiceImpl(ViewPrinterService printerService,
                           ViewReaderService readerService,
                           ClassLoaderService classLoaderService) {
        this.printerService = printerService;
        this.readerService = readerService;
        this.classLoaderService = classLoaderService;
    }

    @Override
    public Integer welcome() {
        print("views/welcome.txt");
        return readerService.readInteger();
    }

    @Override
    public Integer configurePrimeListWithLengthLimit() {
        return printAngReadIntegerResponse("views/configurePrimeListWithLengthLimit.txt");
    }

    @Override
    public Long configurePrimeListWithLargestNumberLimit() {
        return printAngReadLongResponse("views/configurePrimeListWithLargestNumberLimit.txt");
    }

    @Override
    public Long configureLargestPrime() {
        return printAngReadLongResponse("views/configureLargestPrime.txt");
    }

    private Long printAngReadLongResponse(String resourcePath) {
        print(resourcePath);
        return readerService.readLong();
    }

    private Integer printAngReadIntegerResponse(String resourcePath) {
        print(resourcePath);
        return readerService.readInteger();
    }

    private void print(String resourcePath) {
        String view = classLoaderService.loadResource(resourcePath);

        printerService.print(view);
    }

    @Override
    public void response(List<Long> primeList) {
        String view = classLoaderService.loadResource("views/response.txt");

        printerService.print(view, primeList);
        readerService.waitSomeInput();
    }

    @Override
    public void error(String errorMessage) {
        String view = classLoaderService.loadResource("views/error.txt");

        printerService.print(view, errorMessage);
        readerService.waitSomeInput();
    }

    @Override
    public void close() throws IOException {
        printerService.close();
        readerService.close();
    }
}
