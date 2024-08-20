package com.offerista.producer.repository;

import com.offerista.producer.service.writer.DiskWriterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.Writer;
import java.util.List;

class RandomNumberDiskRepositoryTest {

    @Mock
    private DiskWriterService diskWriterService;

    private RandomNumberDiskRepository testSubject;

    @BeforeEach
    void setUp() throws Exception {
        try (var ignored = MockitoAnnotations.openMocks(this)) {
            testSubject = new RandomNumberDiskRepository(diskWriterService);
        }
    }

    @Test
    void test_save_normal_callsDiskWriterSave() {
        var mockContent = List.of(1);
        var expectedContent = List.of("1");

        testSubject.save(mockContent);

        Mockito.verify(diskWriterService).write(Mockito.any(Writer.class), Mockito.eq(expectedContent));
    }
}