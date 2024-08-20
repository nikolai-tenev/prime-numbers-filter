package com.offerista.producer.service.writer;

import de.siegmar.fastcsv.writer.CsvWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

class CsvWriterServiceTest {
    @Mock
    private CsvWriter csvWriter;
    @Mock
    private CsvWriter.CsvWriterBuilder csvWriterBuilder;

    private CsvWriterService testSubject;

    @BeforeEach
    void setUp() throws Exception {
        try (var ignored = MockitoAnnotations.openMocks(this)) {
            testSubject = new CsvWriterService(csvWriterBuilder);
        }
    }

    @Test
    void test_write_normal_savesToFile() throws IOException {
        try (var expectedFile = Files.newBufferedWriter(Paths.get("temp_test_file.tmp"), StandardCharsets.UTF_8,
                StandardOpenOption.CREATE)) {
            var expectedContent = List.of("1");

            Mockito.when(csvWriterBuilder.build(expectedFile)).thenReturn(csvWriter);

            testSubject.write(expectedFile, expectedContent);

            Mockito.verify(csvWriter).writeRecord(expectedContent);
            Mockito.verify(csvWriter).close();
        }
    }
}