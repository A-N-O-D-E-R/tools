package com.anode.tool.print;

import java.util.List;


/** Bar code printing interface. */
public interface BarCodePrinter extends Printer {

	/**
	 * Print bar codes with specified copies.
	 * @param barCodeList bar code to print
	 * @param copies minimum 1
	 * @param blockCopies if true print like this 1,1,1,2,2,2,3,3,3 else like this 1,2,3,1,2,3,1,2,3
	 * @throws PrintServicesException if printing fails
	 */
	void printBarCode(List<String> barCodeList, int copies, boolean blockCopies) throws PrintServicesException;

    /**
     * Calibrates the printer.
     * @throws PrintServicesException if calibration fails
     */
    void calibrate() throws PrintServicesException;
}