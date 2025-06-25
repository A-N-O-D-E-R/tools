package com.anode.print;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.anode.print.zebra.TLP3842;

public class CommandLinePrint {

	public static void main(String[] args) throws PrintServicesException, IOException {
		TLP3842 printer = new TLP3842("Zebra 3842", "rdc", args[0], Integer.valueOf(args[1]), Integer.valueOf(args[3]), Integer.valueOf(args[4]), Integer.valueOf(args[5]), Integer
				.valueOf(args[6]), Integer.valueOf(args[7]), Integer.valueOf(args[8]));

		if (args[2].trim().equals("calibrate"))
			printer.calibrate();
		else
			print(printer, new File(args[2]));
	}

	private static void print(TLP3842 printer, File instructionFile) throws PrintServicesException, IOException {

		List<String> labels = new LinkedList<String>();

		String date = new SimpleDateFormat("ddMMyy").format(new Date());

		printer.reset();

		for (String line : getLines(instructionFile))
			labels.add(line.replace("?date?", date));

		printer.printBarCode(labels, 1, false);
	}

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
