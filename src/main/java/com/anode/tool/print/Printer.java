package com.anode.tool.print;

/**
 * Interface for printer implementations.
 */
public interface Printer {
	/**
	 * Gets the printer name.
	 * @return the printer name
	 */
	public abstract String getName();

	/**
	 * Gets the printer location.
	 * @return the printer location
	 */
	public abstract String getLocation();

	/**
	 * Gets the printer IP address.
	 * @return the printer IP address
	 */
	public abstract String getIpAdress();

	/**
	 * Gets the printer port.
	 * @return the printer port
	 */
	public abstract Integer getPort();

	/**
	 * Resets the printer.
	 * @throws PrintServicesException if the reset operation fails
	 */
	public abstract void reset() throws PrintServicesException;
}