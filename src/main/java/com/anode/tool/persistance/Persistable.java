package com.anode.tool.persistance;

import java.io.Serializable;

/**
 * Interface for objects that can be persisted with a unique identifier.
 */
public interface Persistable {
    /**
     * Gets the unique identifier of this persistable object.
     * @return the object's identifier
     */
    public Serializable getId();

    /**
     * Sets the unique identifier of this persistable object.
     * @param id the identifier to set
     */
    public void setId(Serializable id);
}
