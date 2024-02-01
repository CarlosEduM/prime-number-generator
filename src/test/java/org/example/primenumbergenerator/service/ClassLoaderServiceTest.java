package org.example.primenumbergenerator.service;

import org.example.PrimeNumberGenerator;
import org.example.primenumbergenerator.exception.PrimeGeneratorException;
import org.example.primenumbergenerator.service.impl.ClassLoaderServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ClassLoaderServiceTest {
    private static ClassLoaderService classLoaderService;

    @BeforeAll
    static void beforeAll() {
        classLoaderService = new ClassLoaderServiceImpl(PrimeNumberGenerator.class.getClassLoader());
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
    void loadResource_getFromCache() {
        ClassLoader classLoader = Mockito.spy(PrimeNumberGenerator.class.getClassLoader());
        ClassLoaderServiceImpl classLoaderService = new ClassLoaderServiceImpl(classLoader);

        classLoaderService.loadResource("views/test_empty.txt");
        classLoaderService.loadResource("views/test_empty.txt");
        verify(classLoader, times(1)).getResourceAsStream(any());
    }

    @Test
    void loadResource_classloaderException() throws IOException {
        ClassLoader classLoader = mock(ClassLoader.class);
        InputStream inputStream = mock(InputStream.class);

        ClassLoaderServiceImpl classLoaderService = new ClassLoaderServiceImpl(classLoader);
        when(classLoader.getResourceAsStream(any())).thenReturn(inputStream);
        doThrow(IOException.class).when(inputStream).close();

        assertThrows(PrimeGeneratorException.class, () ->
                classLoaderService.loadResource("views/test_empty.txt"));
    }

    @Test
    void loadResource_resourceDoNotExist() {
        assertThrows(PrimeGeneratorException.class, () -> classLoaderService.loadResource("test_fail.txt"));
    }
}