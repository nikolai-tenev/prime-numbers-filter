package com.offerista.consumer.configuration;

import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.writer.CsvWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CsvConfiguration {
    @Bean
    public CsvWriter.CsvWriterBuilder csvWriterBuilder() {
        return CsvWriter.builder();
    }
    @Bean
    public CsvReader.CsvReaderBuilder csvReaderBuilder() {
        return CsvReader.builder();
    }
}
