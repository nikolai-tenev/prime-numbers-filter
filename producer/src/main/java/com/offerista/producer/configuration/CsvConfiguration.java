package com.offerista.producer.configuration;

import de.siegmar.fastcsv.writer.CsvWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CsvConfiguration {
    @Bean
    public CsvWriter.CsvWriterBuilder csvWriterBuilder() {
        return CsvWriter.builder();
    }
}
