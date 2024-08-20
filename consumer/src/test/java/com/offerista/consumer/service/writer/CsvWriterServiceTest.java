package com.offerista.consumer.service.writer;

import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRecord;
import de.siegmar.fastcsv.writer.CsvWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

class CsvWriterServiceTest {

    @Mock
    private CsvWriter.CsvWriterBuilder csvWriterBuilder;
    @Mock
    private CsvReader.CsvReaderBuilder csvReaderBuilder;
    @Mock
    private CsvWriter csvWriter;
    @Mock
    private CsvReader<CsvRecord> csvReader;
    @Mock
    private Stream<CsvRecord> csvReaderStream;

    private CsvWriterService testSubject;

    @BeforeEach
    void setUp() throws Exception {
        try (var ignored = MockitoAnnotations.openMocks(this)) {
            testSubject = new CsvWriterService(csvWriterBuilder, csvReaderBuilder);
        }
    }

    @Test
    void test_write_normal_savesToCsv() throws IOException {
        var mockPath = Paths.get("");
        var mockExisting = List.of("1");
        var expected = List.of("1", "2");
        Mockito.when(csvWriterBuilder.build(mockPath)).thenReturn(csvWriter);
        Mockito.when(csvReaderBuilder.ofCsvRecord(mockPath)).thenReturn(csvReader);
        Mockito.when(csvReader.stream()).thenReturn(csvReaderStream);
        Mockito.when(csvReaderStream.map(Mockito.any())).thenReturn(Stream.of(mockExisting));

        testSubject.write(mockPath, "2");

        Mockito.verify(csvWriter).writeRecord(expected);
        Mockito.verify(csvWriter).close();
    }

    @Test
    void test_write_badPath_doesNothing() throws IOException {
        var mockPath = Paths.get("non-existing");
        var mockExisting = List.of("1");
        Mockito.when(csvWriterBuilder.build(mockPath)).thenReturn(csvWriter);
        Mockito.when(csvReaderBuilder.ofCsvRecord(mockPath)).thenReturn(csvReader);
        Mockito.when(csvReader.stream()).thenReturn(csvReaderStream);
        Mockito.when(csvReaderStream.map(Mockito.any())).thenReturn(Stream.of(mockExisting));

        testSubject.write(mockPath, "2");

        Mockito.verifyNoInteractions(csvWriter);
    }

    @Test
    void test_write_noContent_doesNothing() throws IOException {
        var mockPath = Paths.get("");
        var mockExisting = List.of("1");
        Mockito.when(csvWriterBuilder.build(mockPath)).thenReturn(csvWriter);
        Mockito.when(csvReaderBuilder.ofCsvRecord(mockPath)).thenReturn(csvReader);
        Mockito.when(csvReader.stream()).thenReturn(csvReaderStream);
        Mockito.when(csvReaderStream.map(Mockito.any())).thenReturn(Stream.of(mockExisting));

        testSubject.write(mockPath, "");

        Mockito.verifyNoInteractions(csvWriter);
    }
}