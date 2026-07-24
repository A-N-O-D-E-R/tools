package com.anode.tool.event;

/**
 * Generic adapter to extract a numeric value from an event.
 */
public interface EventAdapter {
    /**
     * Extracts a numeric value from the given event.
     * @param event the event to extract a value from
     * @return the extracted numeric value
     */
    public double getValue(Event event);
}
