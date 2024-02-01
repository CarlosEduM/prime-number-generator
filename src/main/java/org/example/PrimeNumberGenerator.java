package org.example;

import org.example.primenumbergenerator.config.AppConfig;
import org.example.primenumbergenerator.controller.PrimeGeneratorController;

import java.io.IOException;

public class PrimeNumberGenerator {
    public static void main(String[] args) throws IOException {
        try (AppConfig appConfig = new AppConfig()) {
            PrimeGeneratorController primeGeneratorController = appConfig.getPrimeGeneratorController();

            while (primeGeneratorController.welcome());
        }
    }
}
