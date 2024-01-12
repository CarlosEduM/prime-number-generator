package org.example.primenumbergenerator.service;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

public interface ViewPrinterService extends Closeable {
    void print(String input) throws IOException;

    void print(String input, List<?> parameters) throws IOException;

    Integer readInteger() throws IOException;
}
