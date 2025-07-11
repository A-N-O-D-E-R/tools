package com.anode.tool.event;

public interface EventFieldGetter<T extends Event, C> {
	public C get(T event) ;
}
