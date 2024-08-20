package com.offerista.producer.repository;

import com.offerista.producer.service.writer.DiskWriterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.List;

@Repository
public class RandomNumberDiskRepository implements RandomNumberRepository {
    private static final String FILE_PATH = "buffer-logs/";
    private static final String FILE_SUFFIX = "_buffer-logs.csv";

    private static final Logger LOG = LoggerFactory.getLogger(RandomNumberDiskRepository.class);

    private final DiskWriterService diskWriterService;

    @Autowired
    public RandomNumberDiskRepository(DiskWriterService diskWriterService) {
        this.diskWriterService = diskWriterService;
    }

    /**
     * Saves the new batch of random numbers to disk.
     *
     * @param newNumbersBatch the random numbers to save
     */
    @Override
    public void save(List<Integer> newNumbersBatch) {
        try {
            var bufferLogsPath = Paths.get(FILE_PATH);

            if (Files.notExists(bufferLogsPath)) {
                Files.createDirectory(bufferLogsPath);
            }
        } catch (IOException e) {
            //TODO send to a logging, error capturing or metrics service, e.g. Sentry, Grafana Cloud, etc.
            LOG.error("Cannot create the directory for buffer logs.", e);
        }

        var filePath = FILE_PATH + Instant.now().toEpochMilli() + FILE_SUFFIX;

        try {
            diskWriterService.write(
                    Files.newBufferedWriter(Paths.get(filePath), StandardCharsets.UTF_8, StandardOpenOption.CREATE),
                    newNumbersBatch.stream().map(String::valueOf).toList()
            );

            LOG.debug("Buffer saved to disk, path: {}", filePath);
        } catch (IOException e) {
            //TODO send to a logging, error capturing or metrics service, e.g. Sentry, Grafana Cloud, etc.
            LOG.error("Error occurred while saving buffer to file, path: {}", filePath, e);
        }
    }
}
