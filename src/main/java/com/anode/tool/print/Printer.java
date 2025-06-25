package com.anode.print;

public interface Printer {
	public abstract String getName();
	public abstract String getLocation();
	public abstract String getIpAdress();
	public abstract Integer getPort();
	public abstract void reset() throws PrintServicesException;
}