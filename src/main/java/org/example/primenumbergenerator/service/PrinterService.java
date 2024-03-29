package org.example.primenumbergenerator.service;

import java.io.Closeable;
import java.util.List;

public interface PrinterService extends Closeable {
    void print(String input);

    void print(String input, List<?> parameters);

    void print(String input, String message);
}
