package org.example.primenumbergenerator.service;

import org.example.primenumbergenerator.exception.PrimeGeneratorException;
import org.example.primenumbergenerator.service.impl.ClassLoaderServiceImpl;
import org.example.primenumbergenerator.service.impl.ViewPrinterServiceImpl;
import org.example.primenumbergenerator.service.impl.ViewReaderServiceImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.*;

class ClassLoaderServiceTest {
    private static ClassLoaderService classLoaderService;

    @BeforeAll
    static void beforeAll() {
        classLoaderService = new ClassLoaderServiceImpl();
    }

    @Test
    void loadResource_happyPath() {
        String resource = classLoaderService.loadResource("views/test_oneLine.txt");

        assertEquals("test\n", resource);
    }

    @Test
    void loadResource_twoLines() {
        String resource = classLoaderService.loadResource("views/test_twoLines.txt");

        assertEquals("test\ntest\n", resource);
    }

    @Test
    void loadResource_emptyFile() {
        String resource = classLoaderService.loadResource("views/test_empty.txt");

        assertEquals("", resource);
    }

    @Test
    void loadResource_resourceDoNotExist() {
        assertThrows(PrimeGeneratorException.class, () -> classLoaderService.loadResource("test_fail.txt"));
    }
}