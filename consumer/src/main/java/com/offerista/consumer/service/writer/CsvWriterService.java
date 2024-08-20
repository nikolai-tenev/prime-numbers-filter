package com.offerista.consumer.service.writer;

import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRecord;
import de.siegmar.fastcsv.writer.CsvWriter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class CsvWriterService implements DiskWriterService {

    /**
     * Method that appends data to a csv file on disk. This method writes all data on a single row.
     *
     * @param filePath to save the data to
     * @param content  data to save
     * @throws IOException
     */
    @Override
    public void write(Path filePath, String content) throws IOException {
        try (var csvReader = CsvReader.builder().ofCsvRecord(filePath)) {
            var prevValues = csvReader.stream().map(CsvRecord::getFields).flatMap(Collection::stream)
                    .collect(Collectors.toList());
            prevValues.add(content);

            var csvWriter = CsvWriter.builder().build(filePath);
            csvWriter.writeRecord(prevValues);
            csvWriter.close();
        }
    }
}
