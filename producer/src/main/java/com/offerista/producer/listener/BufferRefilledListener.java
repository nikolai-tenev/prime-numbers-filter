package com.offerista.producer.listener;

import com.offerista.producer.event.BufferRefilledEvent;
import com.offerista.producer.repository.RandomNumberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class BufferRefilledListener {
    private static final Logger LOG = LoggerFactory.getLogger(BufferRefilledListener.class);

    private final RandomNumberRepository randomNumberRepository;

    @Autowired
    public BufferRefilledListener(RandomNumberRepository randomNumberRepository) {
        this.randomNumberRepository = randomNumberRepository;
    }

    /**
     * Listener that reacts to every random number buffer refill and saves the new numbers.
     * @param bufferRefilledEvent the fired event
     */
    @EventListener
    public void onBufferRefilled(BufferRefilledEvent bufferRefilledEvent) {
        LOG.debug("Buffer refilled, saving new values.");

        randomNumberRepository.save(bufferRefilledEvent.newValues());
    }
}
