package f21as.group2.model;

import f21as.group2.controller.*;
import f21as.group2.interfaces.FileProcessor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;

/*
 * Crossing Phase class
 * This class stores the attributes and methods for phases and its durations.
 * The data structure used is a TreeMap where the key is the phase number and the 
 * value is the duration.
 */

public class Phase implements FileProcessor {
	// Creating the phaseTable as a TreeMap with a String key and interger value
	TreeMap<String, Integer> phaseTable;

	// To add the values of the TreeMap to the ArrayList
	private Collection<Integer> values;
	private List<Integer> listValues;

	// Constructor to initialise the phaseTable
	public Phase() {
		phaseTable = new TreeMap<String, Integer>();
	}

	// Getter and setter
	public TreeMap<String, Integer> getPhaseTable() {
		return phaseTable;
	}

	public void setPhaseTable(TreeMap<String, Integer> phaseTable) {
		this.phaseTable = phaseTable;
	}

	// To get the value (duration) of the key
	public int getValue(String pKey) {
		return phaseTable.get(pKey);
	}

	// To add up the durations of all phases
	public int getTotalDuration() {
		return phaseTable.get("P1") + phaseTable.get("P2") + phaseTable.get("P3") + phaseTable.get("P4")
				+ phaseTable.get("P5") + phaseTable.get("P6") + phaseTable.get("P7") + phaseTable.get("P8");
	}

	// To add the values in the TreeMap up until the passed phase number
	public int getTotalUpto(int num) {
		int total = 0;
		int i = 0;

		values = phaseTable.values();
		listValues = new ArrayList<Integer>(values);

		// To add the values
		for (Integer integer : listValues) {
			while (i < num) {
				total = total + listValues.get(i);
				i++;
			}
		}
		return total;
	}

	// To return the tree map with the key and value
	public TreeMap<String, Integer> returnData() {
		// To create a new TreeMap
		TreeMap<String, Integer> resultMap = new TreeMap<>();

		// To insert the data of PhaseTable into the new TreeMap
		for (String key : phaseTable.keySet()) {
			resultMap.put(key, phaseTable.get(key));
		}

		return resultMap;
	}

	// To read the Intersection file
	public void readFile(String fileName) {
		try {
			File file = new File(fileName);
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				// To read the current line and process it
				String line = scanner.nextLine();

				// If the line is not empty
				if (line.length() != 0) {
					// To process the line
					processLine(line, fileName);
				}
			}
			scanner.close();

			// To add values to the ArrayList
			values = phaseTable.values();
			listValues = new ArrayList<Integer>(values);
		}
		// If the csv file is not found, normal exit the system
		catch (FileNotFoundException fnf) {
			String message = fileName
					+ " not found. Cannot process the data from the file. Please rename the file to \"Intersection.csv\"\n";
			LogManager.getLogger().severe(message);
			System.exit(0);
		}
	}

	// To process the data read from the file
	public void processLine(String line, String fileName) {
		try {
			// Split the line when ',' is read and store it in an array
			String parts[] = line.split(",");

			/*
			 * To process Phase Number. To remove line breaks from the first line. The first
			 * line read from the file has a line break by default which needs to be removed
			 */
			String pnum = parts[0].trim().replace("\uFEFF", "");
			pnum = pnum.toUpperCase();

			// To process Duration
			String spDur = parts[1].trim();
			int pDur = Integer.parseInt(spDur);

			// To add the phase number and duration into the phaseTable
			phaseTable.put(pnum, pDur);
		}
		// Exceptions for Process Line
		// If the data type is incorrect
		catch (NumberFormatException nfe) {
			String message = "Number conversion error in the line: " + line + " - " + nfe.getMessage() + "\n";
			LogManager.getLogger().info(message);
		}
		// If there are missing values in a line
		catch (ArrayIndexOutOfBoundsException aib) {
			String message = "Not enough items in the line: " + line + " - at index position: " + aib.getMessage() +"\n";
			LogManager.getLogger().info(message);
		}
	}
}
