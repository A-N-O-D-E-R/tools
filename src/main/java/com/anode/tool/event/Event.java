package com.anode.tool.event;

import java.util.Date;
import java.util.UUID;

import lombok.Getter;

/**
 * Abstract base class for events in the event system.
 * Each event has a unique identifier and a timestamp.
 */
@Getter
public abstract class Event implements Comparable<Event> {
    /**
     * Unique identifier for the event.
     */
    private UUID id;

    /**
     * Timestamp of when the event occurred.
     */
    private Date date;
}
