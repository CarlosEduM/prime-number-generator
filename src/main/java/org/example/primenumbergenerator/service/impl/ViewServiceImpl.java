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
        String welcome = classLoaderService.loadResource("views/welcome.txt");

        printerService.print(welcome);
        return readerService.readInteger();
    }

    @Override
    public void response(List<Integer> primeList) {
        String response = classLoaderService.loadResource("views/response.txt");

        printerService.print(response, primeList);
        readerService.waitSomeInput();
    }

    @Override
    public void close() throws IOException {
        printerService.close();
        readerService.close();
    }
}
