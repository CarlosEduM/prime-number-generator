package org.example.primenumbergenerator.service;

import java.io.Closeable;
import java.io.IOException;

public interface ViewService extends Closeable {
    void print(String input) throws IOException;
    Integer readInteger() throws IOException;
}
