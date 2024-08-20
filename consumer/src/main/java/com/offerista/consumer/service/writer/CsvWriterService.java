package com.offerista.consumer.service.writer;

import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRecord;
import de.siegmar.fastcsv.writer.CsvWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class CsvWriterService implements DiskWriterService {
    private static final Logger LOG = LoggerFactory.getLogger(CsvWriterService.class);

    private final CsvWriter.CsvWriterBuilder csvWriterBuilder;
    private final CsvReader.CsvReaderBuilder csvReaderBuilder;

    @Autowired
    public CsvWriterService(CsvWriter.CsvWriterBuilder csvWriterBuilder, CsvReader.CsvReaderBuilder csvReaderBuilder) {
        this.csvWriterBuilder = csvWriterBuilder;
        this.csvReaderBuilder = csvReaderBuilder;
    }

    /**
     * Method that appends data to a csv file on disk. This method writes all data on a single row.
     *
     * @param filePath to save the data to
     * @param content  data to save
     * @throws IOException
     */
    @Override
    public void write(Path filePath, String content) throws IOException {
        if (!Files.exists(filePath)) {
            //TODO send to a logging, error capturing or metrics service, e.g. Sentry, Grafana Cloud, etc.
            LOG.error("Invalid file path: {}", filePath);
            return;
        }

        if (!StringUtils.hasText(content)) {
            return;
        }

        try (var csvReader = csvReaderBuilder.ofCsvRecord(filePath)) {
            var prevValues = csvReader.stream().map(CsvRecord::getFields).flatMap(Collection::stream)
                    .collect(Collectors.toList());
            prevValues.add(content);

            var csvWriter = csvWriterBuilder.build(filePath);
            csvWriter.writeRecord(prevValues);
            csvWriter.close();
        }
    }
}
