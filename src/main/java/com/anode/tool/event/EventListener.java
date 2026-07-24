package com.anode.tool.event;

/**
 * Interface for receiving and handling events.
 */
public interface EventListener {

    /**
     * Handles an event.
     * @param event the event to handle
     */
    public void handleEvent(Event event);

    /**
     * Called when the connection to the event producer has been closed.
     */
    public void linkClosed();

    /**
     * Called when the connection to the event producer has been lost unexpectedly.
     */
    public void linkBroken();

    /**
     * Called when the connection to the event producer has experienced disruptions
     * and events may have been lost.
     */
    public void linkDamaged();

}
