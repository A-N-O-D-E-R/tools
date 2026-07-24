package com.anode.tool.event;

import java.io.Serializable;

/**
 * Interface for filtering events before delivery to listeners.
 */
public interface EventListenerFilter extends Serializable {

    /**
     * Determines whether an event should be delivered to a listener.
     * @param event the event to check
     * @return true if the event should be delivered to the listener, false otherwise
     */
    public boolean isEventEnabled(Event event);
}
