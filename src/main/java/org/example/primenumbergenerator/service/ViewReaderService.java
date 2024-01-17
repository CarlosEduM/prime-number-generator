package org.example.primenumbergenerator.service;

import java.io.Closeable;
import java.io.IOException;

public interface ViewReaderService extends Closeable {
    Integer readInteger();
}
