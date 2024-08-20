package com.offerista.consumer.repository;

public interface PrimeNumbersRepository {
    /**
     * Saves the passed primeNumber to disk.
     *
     * @param primeNumber to save
     */
    void save(Integer primeNumber);
}
