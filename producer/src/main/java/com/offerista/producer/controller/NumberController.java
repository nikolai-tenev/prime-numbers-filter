package com.offerista.producer.controller;

import com.offerista.producer.service.supplier.NumberSequenceSupplierService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.ArrayList;

@RestController
@RequestMapping("/number")
public class NumberController {
    private static final int MAX_STREAM_ITEM_CNT = 5;
    private static final int STREAM_INTERVAL_SECONDS = 1;
    private static final String SSE_EVENT_NAME = "periodic-event";
    private static final Logger LOG = LoggerFactory.getLogger(NumberController.class);

    private final NumberSequenceSupplierService numberSequenceSupplierService;

    @Autowired
    public NumberController(NumberSequenceSupplierService numberSequenceSupplierService) {
        this.numberSequenceSupplierService = numberSequenceSupplierService;
    }

    /**
     * API Endpoint that returns a stream of random numbers.
     *
     * @return the stream
     */
    @GetMapping("/random/stream")
    public Flux<ServerSentEvent<String>> randomStream() {
        return Flux.interval(Duration.ofSeconds(STREAM_INTERVAL_SECONDS))
                .map(intervalId -> {
                    var result = new ArrayList<Integer>();

                    for (var i = 0; i < MAX_STREAM_ITEM_CNT; i++) {
                        result.add(numberSequenceSupplierService.getNext());
                    }

                    return result;
                })
                .flatMap(Flux::fromIterable)
                .map(randomNumber -> {
                    LOG.debug("Sending random number: {}", randomNumber);
                    return ServerSentEvent.<String>builder()
                            .id(String.valueOf(randomNumber))
                            .event(SSE_EVENT_NAME)
                            .data(String.valueOf(randomNumber))
                            .build();
                });
    }
}
