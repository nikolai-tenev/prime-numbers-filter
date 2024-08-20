package com.offerista.consumer.repository;

import com.offerista.consumer.service.writer.DiskWriterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.nio.file.Path;

class PrimeNumbersDiskRepositoryTest {

    @Mock
    private DiskWriterService diskWriterService;

    private PrimeNumbersDiskRepository testSubject;

    @BeforeEach
    void setUp() throws Exception {
        try (var ignored = MockitoAnnotations.openMocks(this)) {
            testSubject = new PrimeNumbersDiskRepository(diskWriterService);
        }
    }

    @Test
    void test_save_normal_callsDiskWriterSave() throws IOException {
        var mockContent = 1;
        var expectedContent = "1";

        testSubject.save(mockContent);

        Mockito.verify(diskWriterService).write(Mockito.any(Path.class), Mockito.eq(expectedContent));
    }

    @Test
    void test_save_nullNumber_doesNothing() {
        testSubject.save(null);

        Mockito.verifyNoInteractions(diskWriterService);
    }
}