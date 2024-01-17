package org.example.primenumbergenerator.service;

import java.io.Closeable;

public interface ViewReaderService extends Closeable {
    Integer readInteger();

    void waitSomeInput();
}
