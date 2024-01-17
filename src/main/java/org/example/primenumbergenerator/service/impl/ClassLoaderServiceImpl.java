package org.example.primenumbergenerator.service.impl;

import org.example.PrimeNumberGenerator;
import org.example.primenumbergenerator.exception.PrimeGeneratorException;
import org.example.primenumbergenerator.service.ClassLoaderService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

public class ClassLoaderServiceImpl implements ClassLoaderService {
    private static final ClassLoader CLASS_LOADER = PrimeNumberGenerator.class.getClassLoader();
    private static final Map<String, String> cache = new HashMap<>();


    @Override
    public String loadResource(String resourcePath) {
        String resource = cache.get(resourcePath);

        if (Objects.nonNull(resource))
            return resource;

        resource = scanResource(resourcePath);
        cache.put(resourcePath, resource);
        return resource;
    }

    private static String scanResource(String resourcePath) {
        try (InputStream resource = CLASS_LOADER.getResourceAsStream(resourcePath)) {
            return tryToScanResource(resourcePath, resource);
        } catch (IOException e) {
            throw new PrimeGeneratorException(e);
        }
    }

    private static String tryToScanResource(String resourcePath, InputStream resource) {
        if (resource == null)
            throw new PrimeGeneratorException("Some problem when trying to load '" + resourcePath + "'");

        try (Scanner scanner = new Scanner(resource, StandardCharsets.UTF_8)) {
            return scanAll(scanner);
        }
    }

    private static String scanAll(Scanner scanner) {
        StringBuilder builder = new StringBuilder();

        while (scanner.hasNextLine()) {
            builder.append(scanner.nextLine());
            builder.append("\n");
        }

        return builder.toString();
    }
}
