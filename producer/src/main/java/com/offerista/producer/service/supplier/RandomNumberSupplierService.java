package com.offerista.producer.service.supplier;

import com.offerista.producer.event.BufferRefilledEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Buffered random number service. Fires a buffer refill event whenever new values are generated and put into the
 * buffer.
 */
@Service
public class RandomNumberSupplierService implements NumberSequenceSupplierService {
    private static final Logger LOG = LoggerFactory.getLogger(RandomNumberSupplierService.class);

    private static final int BUFFER_UPPER_LIMIT = 100;
    private static final int BUFFER_LOWER_LIMIT = 10;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final Queue<Integer> buffer = new LinkedList<>();
    private final SecureRandom prng = new SecureRandom();

    @Autowired
    public RandomNumberSupplierService(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * Gets the next random number in the sequence.
     *
     * @return the random number
     */
    @Override
    public Integer getNext() {
        if (buffer.size() <= BUFFER_LOWER_LIMIT) {
            LOG.debug("Buffer size is: {}, refilling...", buffer.size());
            fillBuffer(BUFFER_UPPER_LIMIT - buffer.size());
        }

        return buffer.poll();
    }

    /**
     * Refill the random number buffer and fire the buffer refill event.
     *
     * @param fillLimit how much numbers to add.
     */
    private void fillBuffer(int fillLimit) {
        var randomNumbersForEvent = new ArrayList<Integer>();

        for (int i = 0; i < fillLimit; i++) {
            var randomNumber = prng.nextInt(0, Integer.MAX_VALUE);
            buffer.offer(randomNumber);
            randomNumbersForEvent.add(randomNumber);
        }

        applicationEventPublisher.publishEvent(new BufferRefilledEvent(randomNumbersForEvent));
    }
}
