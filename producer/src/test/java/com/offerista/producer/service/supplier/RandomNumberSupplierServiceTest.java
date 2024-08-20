package com.offerista.producer.service.supplier;

import com.offerista.producer.event.BufferRefilledEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;

class RandomNumberSupplierServiceTest {
    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    private RandomNumberSupplierService testSubject;

    @BeforeEach
    void setUp() throws Exception {
        try (var ignored = MockitoAnnotations.openMocks(this)) {
            testSubject = new RandomNumberSupplierService(applicationEventPublisher);
        }
    }

    @Test
    void test_getNext_normal_firesBufferRefillEvent() {
        var result = testSubject.getNext();

        Assertions.assertNotNull(result);
        Mockito.verify(applicationEventPublisher).publishEvent(Mockito.any(BufferRefilledEvent.class));
    }
}