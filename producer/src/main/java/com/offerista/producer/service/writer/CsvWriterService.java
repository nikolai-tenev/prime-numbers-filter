package com.offerista.producer.service.writer;

import de.siegmar.fastcsv.writer.CsvWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;

@Service
public class CsvWriterService implements DiskWriterService {
    private static final Logger LOG = LoggerFactory.getLogger(CsvWriterService.class);

    private final CsvWriter.CsvWriterBuilder csvWriterBuilder;

    @Autowired
    public CsvWriterService(CsvWriter.CsvWriterBuilder csvWriterBuilder) {
        this.csvWriterBuilder = csvWriterBuilder;
    }

    /**
     * Write out a single csv row to a file on disk. Will replace previous data.
     *
     * @param file    path to the file to write to
     * @param content what to write.
     */
    @Override
    public void write(Writer file, Collection<String> content) {
        try {
            var csv = csvWriterBuilder.build(file);
            csv.writeRecord(content);
            csv.close();
        } catch (IOException e) {
            LOG.error("Error writing CSV to file", e);
        }
    }
}
