package org.example.primenumbergenerator.service;

import java.io.Closeable;
import java.util.List;

public interface ViewPrinterService extends Closeable {
    void print(String input);

    void print(String input, List<?> parameters);
}
