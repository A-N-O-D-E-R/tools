package com.anode.tool.print;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.anode.tool.print.zebra.TLP3842;

/**
 * Command line utility for printing bar codes.
 */
public class CommandLinePrint {

	/**
	 * Main entry point for command line printing.
	 * @param args command line arguments
	 * @throws PrintServicesException if printing fails
	 * @throws IOException if an I/O error occurs
	 */
	public static void main(String[] args) throws PrintServicesException, IOException {
		TLP3842 printer = new TLP3842("Zebra 3842", "rdc", args[0], Integer.valueOf(args[1]), Integer.valueOf(args[3]), Integer.valueOf(args[4]), Integer.valueOf(args[5]), Integer
				.valueOf(args[6]), Integer.valueOf(args[7]), Integer.valueOf(args[8]));

		if (args[2].trim().equals("calibrate"))
			printer.calibrate();
		else
			print(printer, new File(args[2]));
	}

    /**
     * Prints bar codes from an instruction file.
     * @param printer the printer to use
     * @param instructionFile the file containing bar code instructions
     * @throws PrintServicesException if printing fails
     * @throws IOException if the file cannot be read
     */
    private static void print(TLP3842 printer, File instructionFile) throws PrintServicesException, IOException {

        List<String> labels = new LinkedList<String>();

        String date = new SimpleDateFormat("ddMMyy").format(new Date());

        printer.reset();

        for (String line : getLines(instructionFile))
            labels.add(line.replace("?date?", date));

        printer.printBarCode(labels, 1, false);
    }

    /**
     * Reads lines from a file, skipping comments and empty lines.
     * @param file the file to read
     * @return a list of non-empty, non-comment lines
     * @throws IOException if the file cannot be read
     */
    private static List<String> getLines(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));

		List<String> lines = new LinkedList<String>();
		try {
			String line;
			while ((line = reader.readLine()) != null) {
				if (!line.trim().startsWith("#") || line.trim().length() > 0)
					lines.add(line);
			}
		} finally {
			reader.close();
		}

		return lines;
	}

}
