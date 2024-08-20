package com.offerista.consumer.repository;

import com.offerista.consumer.service.writer.DiskWriterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Repository
public class PrimeNumbersDiskRepository implements PrimeNumbersRepository {
    private static final String PRIME_NUMBERS_LOGS_PATH = "prime-numbers-logs/";
    private static final Logger LOG = LoggerFactory.getLogger(PrimeNumbersDiskRepository.class);

    private final DiskWriterService diskWriterService;

    @Autowired
    public PrimeNumbersDiskRepository(DiskWriterService diskWriterService) {
        this.diskWriterService = diskWriterService;
    }

    /**
     * Saves the passed primeNumber to disk.
     *
     * @param primeNumber to save
     */
    @Override
    public void save(Integer primeNumber) {
        if (primeNumber == null) {
            return;
        }

        var primeNumbersLogsDir = Paths.get(PRIME_NUMBERS_LOGS_PATH);
        var primeNumbersLogsFile = Paths.get(PRIME_NUMBERS_LOGS_PATH + "prime-numbers-logs.csv");

        try {
            if (Files.notExists(primeNumbersLogsDir)) {
                Files.createDirectory(primeNumbersLogsDir);
            }

            if (Files.notExists(primeNumbersLogsFile)) {
                Files.createFile(primeNumbersLogsFile);
            }
        } catch (IOException e) {
            //TODO send to a logging, error capturing or metrics service, e.g. Sentry, Grafana Cloud, etc.
            LOG.error("Cannot create the directory or the file for prime numbers logs.", e);
        }

        try {
            diskWriterService.write(primeNumbersLogsFile, primeNumber.toString());

            LOG.debug("Prime saved to disk, path: {}", primeNumbersLogsFile);
        } catch (IOException e) {
            //TODO send to a logging, error capturing or metrics service, e.g. Sentry, Grafana Cloud, etc.
            LOG.error("Error occurred while saving prime number to file, path: {}", primeNumbersLogsFile, e);
        }
    }
}
