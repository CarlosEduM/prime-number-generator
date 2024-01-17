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
        String view = classLoaderService.loadResource("views/welcome.txt");

        printerService.print(view);
        return readerService.readInteger();
    }

    @Override
    public Long configurePrimeListWithLengthLimit() {
        return printAngReadResponse("views/configurePrimeListWithLengthLimit.txt");
    }

    @Override
    public Long configurePrimeListWithLargestNumberLimit() {
        return printAngReadResponse("views/configurePrimeListWithLargestNumberLimit.txt");
    }

    @Override
    public Long configureLargestPrime() {
        return printAngReadResponse("views/configureLargestPrime.txt");
    }

    private Long printAngReadResponse(String resourcePath) {
        String view = classLoaderService.loadResource(resourcePath);

        printerService.print(view);
        return readerService.readLong();
    }

    @Override
    public void response(List<Integer> primeList) {
        String view = classLoaderService.loadResource("views/response.txt");

        printerService.print(view, primeList);
        readerService.waitSomeInput();
    }

    @Override
    public void close() throws IOException {
        printerService.close();
        readerService.close();
    }
}
