package com.anode.tool.event;

/**
 * Generic interface for extracting a field of type C from an event of type T.
 * @param <T> the type of event
 * @param <C> the type of field to extract
 */
public interface EventFieldGetter<T extends Event, C> {
    /**
     * Extracts a field of type C from the given event.
     * @param event the event to extract a field from
     * @return the extracted field value
     */
    public C get(T event);
}
