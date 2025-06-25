package com.anode.print;

import java.util.List;


public interface BarCodePrinter extends Printer {

	/**	 
	 * @param barCodeList bar code to print
	 * @param copies minimum 1
	 * @param blockCopies if true print like this 1,1,1,2,2,2,3,3,3 else like this 1,2,3,1,2,3,1,2,3
	 * @throws PrintServiceException
	 */
	public void printBarCode(List<String> barCodeList, int copies, boolean blockCopies) throws PrintServicesException;

	public void calibrate() throws PrintServicesException;
}