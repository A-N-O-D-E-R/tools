package com.anode.print.zebra;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

import com.anode.print.AbstractPrinter;
import com.anode.print.BarCodePrinter;
import com.anode.print.PrintServicesException;

abstract class EPL2Printer extends AbstractPrinter implements BarCodePrinter {

	private int defaultSpeed;
	private int defaultDensity;
	private int dotWidth;
	private int xStartPosition;
	private int yIncrement;
	private int yStartPosition;
	

	/**
	 * @param name
	 * @param location
	 * @param ipAdress
	 * @param port
	 */
	public EPL2Printer(String name, String location, String ipAdress, int port, int defaultSpeed, int defaultDensity, int dotWidth, int xStartPosition, int yStartPosition, int yIncrement) {
		super(name, location, ipAdress, port);
		this.defaultSpeed = defaultSpeed;
		this.defaultDensity = defaultDensity;
		this.dotWidth = dotWidth ;
		this.xStartPosition = xStartPosition ;
		this.yStartPosition = yStartPosition ;
		this.yIncrement = yIncrement ;
	}

	public void reset() throws PrintServicesException {
		sendCommands("^@");

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			System.err.println("While waiting 2000ms before sending commands ");
			e.printStackTrace(System.err);
		}

		StringBuffer commands = new StringBuffer();
		commands.append('S').append(defaultSpeed).append('\n');
		commands.append('D').append(defaultDensity).append('\n');
		commands.append('q').append(dotWidth).append('\n');

		sendCommands(commands.toString());
	}

	public void printBarCode(List<String> barCodeList, int copies, boolean blockCopies) throws PrintServicesException {

		StringBuffer commands = new StringBuffer();

		if (blockCopies) {
			for (int copy = 0; copy < copies; copy++)
				for (int i = 0; i < barCodeList.size(); i++) {
					commands.append("N\n");
					addBarCodeCommands(barCodeList.get(i).toString(), commands);
					commands.append("P1\n");
				}
		} else
			for (int i = 0; i < barCodeList.size(); i++) {
				commands.append("N\n");
				addBarCodeCommands(barCodeList.get(i).toString(), commands);
				commands.append("P").append(copies).append('\n');
			}

		sendCommands(commands.toString());
	}

	private void addBarCodeCommands(String barCode, StringBuffer commandsBuffer) {
		int yPosition = yStartPosition ;
		String[] labels = barCode.split("\\\\") ;
		for(String label : labels) {
			commandsBuffer.append("A"+xStartPosition+","+yPosition+",0,3,1,1,N,\"").append(label).append("\"\n");
			yPosition+=yIncrement ;
		}
	}

	public void sendCommands(String commands) throws PrintServicesException {

		System.out.println("Sending\n" + commands + "\n to " + this);

		Socket printerSocket = new Socket();

		try {
			printerSocket.connect(new InetSocketAddress(getIpAdress(), getPort().intValue()), 5000);

			BufferedOutputStream output = new BufferedOutputStream(printerSocket.getOutputStream());
			output.write(commands.getBytes());
			output.write('\n');
			output.close();

			System.out.println("Done sending commands to " + this);

		} catch (Exception e) {
			throw new PrintServicesException("While sending \n" + commands + "\n to " + this, e);
		} finally {
			try {
				printerSocket.close();
			} catch (IOException e) {
				System.err.println("While closing socket to " + this) ;
				e.printStackTrace(System.err) ;
			}
		}

	}

	public void calibrate() throws PrintServicesException {
		sendCommands("xa");
	}

}
