package com.anode.tool.print;

/**
 * Abstract base class for printer implementations.
 */
public abstract class AbstractPrinter implements Printer {

	private final String name;

	private final String location;

	private final String ipAdress;

	private final Integer port;

	/**
	 * Constructs an AbstractPrinter with the specified parameters.
	 * @param name the printer name
	 * @param location the printer location
	 * @param ipAdress the printer IP address
	 * @param port the printer port number
	 */
	public AbstractPrinter(String name,String location,String ipAdress,Integer port) {
		this.name = name ;
		this.location = location ;
		this.ipAdress = ipAdress ;
		this.port = port ; 
	}

	public String getName() {
		return name;
	}

	public String getLocation() {
		return location;
	}

	public String getIpAdress() {
		return ipAdress;
	}

	public Integer getPort() {
		return port;
	}

	/**
	 * Resets the printer to its initial state.
	 * @throws PrintServicesException if the reset operation fails
	 */
	public abstract void reset() throws PrintServicesException;

	/**
	 * Returns a string representation of this printer.
	 * @return the printer name
	 */
	public String toString() {
		return getName() ;
//		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
//			.append("name", this.name)
//			.append("location", this.location)
//			.append("port", this.port)
//			.append("ipAdress", this.ipAdress)
//			.toString();
	}
}