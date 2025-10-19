package com.anode.tool.service;

import java.io.Serializable;

public interface IdFactory<T extends Serializable> {
    public T newId() ;
	public void consumeId(T id);
}

