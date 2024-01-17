package org.example.primenumbergenerator.service;

import java.io.Closeable;
import java.util.List;

public interface ViewService extends Closeable {
    Integer welcome();
    void response(List<Integer> primeList);
}
