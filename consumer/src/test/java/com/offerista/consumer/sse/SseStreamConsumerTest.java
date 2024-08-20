package com.offerista.consumer.sse;

import com.offerista.consumer.repository.PrimeNumbersRepository;
import com.offerista.consumer.service.prime.number.PrimeNumberCheckerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.reactive.function.client.WebClient;

class SseStreamConsumerTest {
    @Mock
    private PrimeNumberCheckerService primeNumberCheckerService;
    @Mock
    private PrimeNumbersRepository primeNumbersRepository;
    @Mock
    private WebClient.Builder webClientBuilder;

    private SseStreamConsumer testSubject;

    @BeforeEach
    void setUp() throws Exception {
        try (var ignored = MockitoAnnotations.openMocks(this)) {
            testSubject = new SseStreamConsumer(primeNumberCheckerService, primeNumbersRepository, webClientBuilder);
        }
    }

    @Test
    void test_handleEvent_nonPrime_doesntSave() {
        Mockito.when(primeNumberCheckerService.isPrime(Mockito.anyInt())).thenReturn(false);
        var evt = ServerSentEvent.<String>builder()
                .id(String.valueOf(1))
                .event("periodic-event")
                .data(String.valueOf(1))
                .build();

        testSubject.handleEvent(evt);

        Mockito.verify(primeNumberCheckerService).isPrime(1);
        Mockito.verifyNoInteractions(primeNumbersRepository);
    }

    @Test
    void test_handleEvent_prime_saves() {
        Mockito.when(primeNumberCheckerService.isPrime(Mockito.anyInt())).thenReturn(true);
        var evt = ServerSentEvent.<String>builder()
                .id(String.valueOf(2))
                .event("periodic-event")
                .data(String.valueOf(2))
                .build();

        testSubject.handleEvent(evt);

        Mockito.verify(primeNumberCheckerService).isPrime(2);
        Mockito.verify(primeNumbersRepository).save(2);
    }
}