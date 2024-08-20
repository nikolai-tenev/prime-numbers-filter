package com.offerista.producer.event;

import java.util.List;

/**
 * Event that is fired whenever we're refilling the random numbers buffer.
 *
 * @param newValues the new values in the buffer.
 */
public record BufferRefilledEvent(List<Integer> newValues) {
}
