package com.anode.tool.service;

import java.io.Serializable;
import java.util.UUID;

public interface IdFactory {
    public Serializable newId() ;
	public void consumeId(UUID id);
}

