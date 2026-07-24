package com.anode.tool.event;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Date;

/**
 * Interface for managing events, extending EventProducer with notification
 * and archive retrieval capabilities.
 */
public interface EventManager extends EventProducer {

    /**
     * Notifies all listeners of an event.
     * @param event the event to notify listeners about
     */
    public void notify(Event event);

    /**
     * Sends archived events from a starting date to a remote host.
     * @param from the starting date (inclusive) for events to retrieve, or null for all events
     * @param host the IP address of the requesting host
     * @param port the port to write to
     * @throws UnknownHostException if the host cannot be resolved
     * @throws IOException if an I/O error occurs during transmission
     */
    public void sendArchiveEventTo(Date from, String host, int port) throws UnknownHostException, IOException;

    /**
     * Sends archived events in protocol format from a starting date to a remote host.
     * @param from the starting date (inclusive) for events to retrieve, or null for all events
     * @param host the IP address of the requesting host
     * @param port the port to write to
     * @throws UnknownHostException if the host cannot be resolved
     * @throws IOException if an I/O error occurs during transmission
     */
    public void sendProtocoleArchiveEventTo(Date from, String host, int port) throws UnknownHostException, IOException;

}
