package com.anode.print.zebra;

public class TLP3842 extends EPL2Printer {

	/**
	 * @param name
	 * @param location
	 * @param ipAdress
	 * @param port
	 */
	public TLP3842(String name, String location, String ipAdress, int port, int defaultSpeed, int defaultDensity, int dotWidth, int xStartPosition, int yStartPosition, int yIncrement) {
		super(name, location, ipAdress, port, defaultSpeed, defaultDensity, dotWidth, xStartPosition, yStartPosition, yIncrement);
	}

}
