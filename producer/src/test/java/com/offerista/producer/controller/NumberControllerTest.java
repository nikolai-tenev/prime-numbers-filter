package com.offerista.producer.controller;

import com.offerista.producer.service.supplier.NumberSequenceSupplierService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.codec.ServerSentEvent;
import reactor.test.StepVerifier;

class NumberControllerTest {

    @Mock
    private NumberSequenceSupplierService numberSequenceSupplierService;

    private NumberController testSubject;

    @BeforeEach
    void setUp() throws Exception {
        try (var ignored = MockitoAnnotations.openMocks(this)) {
            testSubject = new NumberController(numberSequenceSupplierService);
        }
    }

    @Test
    void test_randomStream_normal_producesStream() {
        var expected = ServerSentEvent.<String>builder()
                .id(String.valueOf(1))
                .event("periodic-event")
                .data(String.valueOf(1))
                .build();
        Mockito.when(numberSequenceSupplierService.getNext()).thenReturn(1);

        var source = testSubject.randomStream();

        StepVerifier
                .create(source)
                .expectNextMatches(evt -> expected.data().equals(evt.data()))
                .thenCancel()
                .verify();
    }
}