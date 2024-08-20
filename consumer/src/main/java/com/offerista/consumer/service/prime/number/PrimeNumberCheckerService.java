package com.offerista.consumer.service.prime.number;

public interface PrimeNumberCheckerService {
    /**
     * Checks if a given number is a prime number.
     *
     * @param number the number to check
     * @return whether or not this number is prime.
     */
    boolean isPrime(Integer number);
}
