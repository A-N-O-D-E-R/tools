package com.anode.tool.event;

//Generic adapter to extract a specific value from an event
public interface EventAdapter {
	public double getValue(Event event) ;
}
