package com.offerista.consumer.service.prime.number;

import org.springframework.stereotype.Service;

import java.util.stream.IntStream;

@Service
public class ParallelPrimeNumberCheckerService implements PrimeNumberCheckerService {
    /**
     * Checks if a given number is a prime number.
     *
     * @param number the number to check
     * @return whether or not this number is prime.
     */
    @Override
    public boolean isPrime(Integer number) {
        if (number == null) {
            return false;
        }

        if (number == Integer.MAX_VALUE || number == 2) {
            return true;
        }

        if (number < 2 || number % 2 == 0) {
            return false;
        }

        int upperLimit = (int) Math.ceil(Math.sqrt(number));

        var isNotPrime = IntStream.iterate(3, (current) -> current <= upperLimit, (current) -> current + 2)
                .parallel()
                .anyMatch(currentNum -> number % currentNum == 0);

        return !isNotPrime;
    }
}
