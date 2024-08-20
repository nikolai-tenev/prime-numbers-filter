package com.offerista.producer.repository;

import java.util.List;

public interface RandomNumberRepository {
    /**
     * Saves the new batch of random numbers to disk.
     *
     * @param newNumbersBatch the random numbers to save
     */
    void save(List<Integer> newNumbersBatch);
}
