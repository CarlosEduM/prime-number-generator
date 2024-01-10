package org.example.primenumbergenerator.math;

import java.util.stream.LongStream;

public class PrimeVerifier {
    public Boolean isAPrimeNumber(Long candidate) {
        if (candidate < 2)
            return false;

        return LongStream.rangeClosed(2L, candidate - 1)
                .noneMatch(divider -> isDivisible(candidate, divider));
    }

    private Boolean isDivisible(Long candidate, Long divider) {
        return (candidate % divider) == 0;
    }
}
