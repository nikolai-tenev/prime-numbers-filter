package com.offerista.consumer.sse;

import com.offerista.consumer.repository.PrimeNumbersRepository;
import com.offerista.consumer.service.prime.number.PrimeNumberCheckerService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.time.LocalTime;

@Component
public class SseStreamConsumer {
    private static final long RETRY_SECONDS = 5;
    private static final Logger LOG = LoggerFactory.getLogger(SseStreamConsumer.class);
    private final PrimeNumberCheckerService primeNumberCheckerService;
    private final PrimeNumbersRepository primeNumbersRepository;
    private final WebClient.Builder webClientBuilder;
    private final String host;
    private final String path;


    @Autowired
    public SseStreamConsumer(PrimeNumberCheckerService primeNumberCheckerService,
            PrimeNumbersRepository primeNumbersRepository, WebClient.Builder webClientBuilder,
            @Value("${app.numberGenerator.host}") String host, @Value("${app.numberGenerator.path}") String path) {
        this.primeNumberCheckerService = primeNumberCheckerService;
        this.primeNumbersRepository = primeNumbersRepository;
        this.webClientBuilder = webClientBuilder;
        this.host = host;
        this.path = path;
    }

    /**
     * Subscribes to a stream of random numbers, checks for primality and saves prime numbers.
     */
    @PostConstruct
    public void startEventConsumer() {
        if (!StringUtils.hasText(host) || !StringUtils.hasText(path)) {
            throw new IllegalArgumentException(
                    "Please specify host and path in application configuration or as env variables.");
        }

        var client = webClientBuilder.baseUrl(host).build();
        var type = new ParameterizedTypeReference<ServerSentEvent<String>>() {
        };

        var eventStream = client.get()
                .uri(path)
                .retrieve()
                .bodyToFlux(type);

        eventStream
                .retryWhen(Retry.fixedDelay(Integer.MAX_VALUE, Duration.ofSeconds(RETRY_SECONDS))
                        .doBeforeRetry(retrySignal ->
                                LOG.info("Error occurred during stream processing, will retry after {} seconds.",
                                        RETRY_SECONDS)
                        ))
                .onErrorContinue((throwable, o) -> LOG.error("Error occurred during stream processing", throwable))
                .subscribe(this::handleEvent,
                        ex -> LOG.error("Error receiving SSE.", ex),
                        () -> LOG.info("Numbers stream completed."));
    }

    void handleEvent(ServerSentEvent<String> content) {
        if (content == null || !StringUtils.hasText(content.data())) {
            return;
        }

        var currentNum = Integer.valueOf(content.data());

        LOG.info("Time: {} - event: name[{}], id [{}], content[{}] ", LocalTime.now(),
                content.event(),
                content.id(), currentNum);

        if (primeNumberCheckerService.isPrime(currentNum)) {
            LOG.info("Found a prime: {}", currentNum);
            primeNumbersRepository.save(currentNum);
        }
    }
}
