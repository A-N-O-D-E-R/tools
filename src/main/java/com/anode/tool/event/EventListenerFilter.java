package com.anode.tool.event;

import java.io.Serializable;

public interface EventListenerFilter extends Serializable {

    /**
     * @param event
     * @return vrai Si l'evevenement doit etre envoye au listener ayant declare
     *         ce filtre
     */
    public boolean isEventEnabled(Event event);
}
