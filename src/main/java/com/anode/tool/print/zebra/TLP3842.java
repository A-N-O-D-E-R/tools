package com.anode.tool.print.zebra;

/**
 * Zebra TLP 3842 printer implementation.
 */
public class TLP3842 extends EPL2Printer {

    /**
     * Constructs a TLP3842 printer with the specified parameters.
     * @param name the printer name
     * @param location the printer location
     * @param ipAdress the printer IP address
     * @param port the printer port
     * @param defaultSpeed the default print speed
     * @param defaultDensity the default print density
     * @param dotWidth the dot width
     * @param xStartPosition the X start position
     * @param yStartPosition the Y start position
     * @param yIncrement the Y increment
     */
    public TLP3842(String name, String location, String ipAdress, int port, int defaultSpeed, int defaultDensity, int dotWidth, int xStartPosition, int yStartPosition, int yIncrement) {
        super(name, location, ipAdress, port, defaultSpeed, defaultDensity, dotWidth, xStartPosition, yStartPosition, yIncrement);
    }

}
