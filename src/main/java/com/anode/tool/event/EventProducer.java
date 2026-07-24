package com.anode.tool.event;

/**
 * Interface for event producers that manage event listeners.
 */
public interface EventProducer {

    /**
     * Adds an event listener with a filter to this producer.
     * @param listener the event listener to add
     * @param filter the filter to apply to events
     */
    public void addEventListener(EventListener listener, EventListenerFilter filter);

    /**
     * Removes an event listener from this producer.
     * @param listener the event listener to remove
     */
    public void removeEventListener(EventListener listener);
}
