package f21as.group2.model;

import f21as.group2.controller.*;
import f21as.group2.interfaces.FileProcessor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

// Holds all Data Collections and statistical methods
public class IntersectionCollection implements FileProcessor {

	// To create an Arraylist for storing vehicles
	private ArrayList<Vehicle> vehicleList;
	// To create an object of Phase
	private static Phase existingPhase;
	// To create an of Segment
	private SegmentList segmentList;
	// For generating vehicles
	private static ScheduledExecutorService ses;
	// Fixed seconds for orange light
	private static int originalDuration;
	// Extended functionailty
	private static AdaptivePhaseControl apc;

	// To create queues for phases
	private static Queue<Vehicle> s1P1StraightRight;
	private static Queue<Vehicle> s1P2Left;
	private static Queue<Vehicle> s2P3StraightRight;
	private static Queue<Vehicle> s2P4Left;
	private static Queue<Vehicle> s3P5StraightRight;
	private static Queue<Vehicle> s3P6Left;
	private static Queue<Vehicle> s4P7StraightRight;
	private static Queue<Vehicle> s4P8Left;

	// ArrayList to store the crossing times
	private ArrayList<Integer> crossingTime1;

	// Constructor to initialise the Array Lists
	public IntersectionCollection() {
		init();
	}

	// To initialise the contstructor
	private void init() {
		// Generate Vehicle & Segment ArrayLists
		this.vehicleList = new ArrayList<Vehicle>();
		this.segmentList = new SegmentList();

		// For threading
		ses = Executors.newScheduledThreadPool(1);

		// Data Added to the Phase List
		// To read the phases from the file and add it to existingPhase
		existingPhase = new Phase();
		existingPhase.readFile("Intersection.csv");

		// To define the queues
		s1P1StraightRight = new LinkedList<>();
		s1P2Left = new LinkedList<>();
		s2P3StraightRight = new LinkedList<>();
		s2P4Left = new LinkedList<>();
		s3P5StraightRight = new LinkedList<>();
		s3P6Left = new LinkedList<>();
		s4P7StraightRight = new LinkedList<>();
		s4P8Left = new LinkedList<>();

		// ArrayList to store the crossing times
		crossingTime1 = new ArrayList<>();
		for (int i = 1; i <= 8; i++) {
			String pNum = "P" + i;
			crossingTime1.add(existingPhase.getValue(pNum));
		}

		// To store the default total duration
		originalDuration = getExistingPhase().getTotalDuration();
		// Extended functionality
		apc = new AdaptivePhaseControl(originalDuration, crossingTime1);
	}

	// Getters & Setters
	public ArrayList<Vehicle> getVehicleList() {
		return vehicleList;
	}

	public void setVehicleList(ArrayList<Vehicle> vehicleList) {
		this.vehicleList = vehicleList;
	}

	public static Phase getExistingPhase() {
		return existingPhase;
	}

	public void setExistingPhase(Phase _existingPhase) {
		existingPhase = _existingPhase;
	}

	public SegmentList getSegmentList() {
		return segmentList;
	}

	public void setSegmentList(SegmentList segmentList) {
		this.segmentList = segmentList;
	}

	public Queue<Vehicle> getQueues(String p) {
		if (p.matches("P1")) {
			return s1P1StraightRight;
		} else if (p.matches("P2")) {
			return s1P2Left;
		} else if (p.matches("P3")) {
			return s2P3StraightRight;
		} else if (p.matches("P4")) {
			return s2P4Left;
		} else if (p.matches("P5")) {
			return s3P5StraightRight;
		} else if (p.matches("P6")) {
			return s3P6Left;
		} else if (p.matches("P7")) {
			return s4P7StraightRight;
		} else {
			return s4P8Left;
		}
	}

	public void setQueues(String p, Queue<Vehicle> queue) {
		if (p.matches("P1")) {
			s1P1StraightRight = queue;
		} else if (p.matches("P2")) {
			s1P2Left = queue;
		} else if (p.matches("P3")) {
			s2P3StraightRight = queue;
		} else if (p.matches("P4")) {
			s2P4Left = queue;
		} else if (p.matches("P5")) {
			s3P5StraightRight = queue;
		} else if (p.matches("P6")) {
			s3P6Left = queue;
		} else if (p.matches("P7")) {
			s4P7StraightRight = queue;
		} else {
			s4P8Left = queue;
		}
	}

	// To get the current phase that is true
	public int getTruePhaseFromCentralController() {
		return SignalController.getTruePhase();
	}

	// To get the remainder time
	public int getRemainingTime() {
		return SignalController.getRemainingTime();
	}

	// To get the ditance from intersection of the vehicle in a queue
	public int findDistance(Vehicle v, Queue<Vehicle> vehicleQueue) {
		int sum = 0;

		for (Vehicle vehicle : vehicleQueue) {
			if (!vehicle.getPlateNumber().equals(v.getPlateNumber())) {
				sum = sum + vehicle.getLength();
			} else {
				return sum;
			}
		}
		return sum;
	}

	// To search for Vehicle by its Plate Number
	public Vehicle findByVehicleNum(String vNum) {
		for (Vehicle v : vehicleList) {
			// If vehicle is found, return vehicle
			if (v.getPlateNumber().equals(vNum)) {
				return v;
			}
		}
		// Else, return null
		return null;
	}

	// To add a vehicle to the ArrayList only if it does not exist already
	public boolean addOneVehicle(Vehicle v) {
		// To find the plate number of the vehicle
		String vNum = v.getPlateNumber();

		// To find the plate number in the list
		Vehicle check = findByVehicleNum(vNum);

		// Check for the existence of plate number
		// If check is null, add the vehicle
		if (check == null) {
			vehicleList.add(v);
			return true;
		}
		// If check is not null, return false (vehicle already exists)
		else {
			return false;
		}
	}

	// To add vehicles into the queue according to their segment number and travel
	// direction if their status is waiting
	public void addToQueue(Vehicle v) {
		if (v.getStatus().equals("Waiting")) {
			if (v.getSegment().equals("S1")
					&& (v.getDirection().equals("Straight") || v.getDirection().equals("Right"))) {
				s1P1StraightRight.add(v);
			} else if (v.getSegment().equals("S1") && v.getDirection().equals("Left")) {
				s1P2Left.add(v);
			} else if (v.getSegment().equals("S2")
					&& (v.getDirection().equals("Straight") || v.getDirection().equals("Right"))) {
				s2P3StraightRight.add(v);
			} else if (v.getSegment().equals("S2") && v.getDirection().equals("Left")) {
				s2P4Left.add(v);
			} else if (v.getSegment().equals("S3")
					&& (v.getDirection().equals("Straight") || v.getDirection().equals("Right"))) {
				s3P5StraightRight.add(v);
			} else if (v.getSegment().equals("S3") && v.getDirection().equals("Left")) {
				s3P6Left.add(v);
			} else if (v.getSegment().equals("S4")
					&& (v.getDirection().equals("Straight") || v.getDirection().equals("Right"))) {
				s4P7StraightRight.add(v);
			} else {
				s4P8Left.add(v);
			}
		}
	}

	// To calculate the total crossing time of vehicles waiting within each phase
	public static int totalCrossTimePerPhase(String passPhase) {
		int crossTime = 0;
		Queue<Vehicle> phaseQueue = new LinkedList<>();

		// To get the queue for the phase
		if (passPhase.equals("P1")) {
			phaseQueue = s1P1StraightRight;
		} else if (passPhase.equals("P2")) {
			phaseQueue = s1P2Left;
		} else if (passPhase.equals("P3")) {
			phaseQueue = s2P3StraightRight;
		} else if (passPhase.equals("P4")) {
			phaseQueue = s2P4Left;
		} else if (passPhase.equals("P5")) {
			phaseQueue = s3P5StraightRight;
		} else if (passPhase.equals("P6")) {
			phaseQueue = s3P6Left;
		} else if (passPhase.equals("P7")) {
			phaseQueue = s4P7StraightRight;
		} else {
			phaseQueue = s4P8Left;
		}

		for (Vehicle v : phaseQueue) {
			// If the status of vehicle is waiting
			if (v.getStatus().equalsIgnoreCase("Waiting")) {
				// Add vehicle's crossing time to total crossing time
				crossTime = crossTime + v.getCrossTime();
			}
		}
		return crossTime;
	}

	// To calculate the number of vehicles waiting to cross each segment
	public int waitingToCrossPerSeg(String passSegment) {
		// Counter
		int c = 0;
		for (Vehicle v : vehicleList) {
			String seg = v.getSegment();
			// If segment matches the passed segment and the status of vehicle is waiting
			if (seg.equalsIgnoreCase(passSegment) && v.getStatus().equalsIgnoreCase("Waiting")) {
				// Increment counter
				c++;
			}
		}
		return c;
	}

	// To calculate the total length of vehicles waiting within each segment
	public int totalLengthPerSeg(String passSegment) {
		int length = 0;
		for (Vehicle v : vehicleList) {
			String seg = v.getSegment();
			// If segment matches the passed segment and the status of vehicle is waiting
			if (seg.equalsIgnoreCase(passSegment) && v.getStatus().equalsIgnoreCase("Waiting")) {
				// Add vehicle's length to total length
				length = length + v.getLength();
			}
		}
		return length;
	}

	// To calculate the total crossing time per segment
	public double avgCrossTimePerSeg(String passSegment) {
		try {
			int time = 0;
			int count = 0;

			for (Vehicle v : vehicleList) {
				String seg = v.getSegment();
				// If segment matches the passed segment and the status of vehicle is waiting
				if (seg.equalsIgnoreCase(passSegment) && v.getStatus().equalsIgnoreCase("Waiting")) {
					// Add vehicle's crossing time to total crossing time
					time = time + v.getCrossTime();
					// Increment counter for total waiting vehicles
					count++;
				}
			}
			if (count == 0)
				return 0;
			// To return the average crossing time for the passed segment
			return (time / count);
		}
		// If count is zero, then / by zero exception
		catch (ArithmeticException e) {
			String message = "There are zero vehicles in the segment. All vehicles have crossed.\n";
			LogManager.getLogger().info(message);
			return 0;
		}
	}

	// To calculate the emission of each segment while waiting
	public double emissionPerSeg(String passSegment) {
		// Counters for car and bus/truck
		int car = 0;
		int busOrTruck = 0;
		double time;
		for (Vehicle v : vehicleList) {
			String seg = v.getSegment();
			// If segment matches the passed segment and the status of vehicle is waiting
			if (seg.equalsIgnoreCase(passSegment) && v.getStatus().equalsIgnoreCase("Waiting")) {
				// Increment car counter if type is car
				if (v.getType().equalsIgnoreCase("Car")) {
					car++;
				}
				// Else, increment counter for bus and truck since both are diesel
				else {
					busOrTruck++;
				}
			}
		}

		// To calculate the waiting time for this segment
		time = totalWaitPerSeg(passSegment);
		/*
		 * Multiply the vehicles with their emission values, convert it into seconds and
		 * multiply by the waiting time
		 */
		return (((car * 5.0) + (busOrTruck * 10.0)) / 60.0) * time;
	}

	// To calculate the total waiting time for vehicles waiting to cross per segment
	public double totalWaitPerSeg(String passSegment) {
		// Phase control
		TreeMap<String, Integer> phaseList = new TreeMap<>();
		phaseList = existingPhase.returnData();

		int sum = 0;
		String check = "";

		for (Segment segment : segmentList.getSegmentList()) {
			// If the segment is not equal to the segment passed
			if (!segment.getSegNum().equalsIgnoreCase(passSegment)) {
				// If the phase number is not the same as check
				if (!segment.getPhaseNumFromSeg().equalsIgnoreCase(check)) {
					// To overwrite the old phase number with the new one
					check = segment.getPhaseNumFromSeg();
					// To add the duration of the phase to sum
					sum += phaseList.get(segment.getPhaseNumFromSeg());
				}
			}
		}
		// To return the total waiting time of the passed segment
		return sum;
	}

	// To calculate the total emissions for vehicles waiting to cross in grams per
	// second
	public double totalEmissions() {
		// To sum emissions for all segments
		double emission = emissionPerSeg("S1") + emissionPerSeg("S2") + emissionPerSeg("S3") + emissionPerSeg("S4");
		// To round off emission to 2 decimal places
		double round = Math.round(emission * 100.0) / 100.0;
		return round;
	}

	// To calculate the average waiting time
	public double avgWaitingTime() {
		// To add the waiting time for all segments and divide them by 4.0
		return (totalWaitPerSeg("S1") + totalWaitPerSeg("S2") + totalWaitPerSeg("S3") + totalWaitPerSeg("S4")) / 4.0;
	}

	// To generate random number plates for vehicles
	private String generateVehiclePlateNumber() {
		Random r = new Random();
		// The number plate will have one random uppercase letter followed by 5 random
		// numbers
		String letter = String.valueOf((char) (r.nextInt(26) + 'A'));
		int num1 = r.nextInt(10) + 0;
		int num2 = r.nextInt(10) + 0;
		int num3 = r.nextInt(10) + 0;
		int num4 = r.nextInt(10) + 0;
		int num5 = r.nextInt(10) + 0;
		// To return the final string
		return letter + num1 + num2 + num3 + num4 + num5;
	}

	// To create a new vehicle with random attributes
	private Vehicle generateNewRandomVehicle() {
		// To generate a vehicle plate number
		String vehicleNumber = generateVehiclePlateNumber();

		Random r = new Random();

		// To randomly set the vehicle type
		String[] types = { "Car", "Bus", "Truck" };
		String type = types[r.nextInt(3)];

		// To set the crossing time between 1-15
		int ct = r.nextInt(15) + 1;

		// To set the direction
		String[] directions = { "Right", "Left", "Straight" };
		String direction = directions[r.nextInt(3)];

		// To set the length according to type
		int l = 0;
		// Car length from 1 to 7
		if (type.matches("Car")) {
			l = r.nextInt(7) + 1;
		}
		// Bus and truck length from 5 to 15
		else {
			l = r.nextInt(8) + 5;
		}

		// To set the emission to zero (temporary)
		int e = 0;

		// To set the segment number
		String[] segments = { "S1", "S2", "S3", "S4" };
		String segment = segments[r.nextInt(4)];

		// To create a new vehicle using the random data
		Vehicle vNew = new Vehicle(vehicleNumber, type, ct, direction, l, e, "Waiting", segment);
		// To set the emission
		vNew.setEmission(type);

		// To check is the vehicle already exists
		boolean ok = addOneVehicle(vNew);
		if (ok == false) {
			// Vehicle already exists
			String message = "The vehicle with the Plate Number " + vehicleNumber + " already exists.\n";
			LogManager.getLogger().info(message);
			return null;
		} else {
			return vNew;
		}
	}

	// To generate new vehicles every 15 seconds and update the Vehicle Table Model
	public void vehicleAutoGenerator() {
		Runnable generate = () -> {
			var vNew = generateNewRandomVehicle();
			addOneVehicle(vNew);
			addToQueue(vNew);
			String message = "New vehicle added -> " + vNew.toString() + "\n";
			LogManager.getLogger().info(message);
		};
		// Scheduling execution of generate at 15 second intervals for the single thread
		ses.scheduleAtFixedRate(generate, 15, 15, TimeUnit.SECONDS);
	}

	// To get the crossing times
	public static void runAdaptivePolicy() {
		ArrayList<Integer> crossingTime = new ArrayList<>();

		// To get the crossing times of all phases
		for (int i = 1; i <= 8; i++) {
			String pNum = "P" + i;
			crossingTime.add(totalCrossTimePerPhase(pNum));
		}

		// To call the method for the extended functionality
		apc.sortForNextCycle(existingPhase.phaseTable, crossingTime);
	}

	// To read the Vehicles file
	public void readFile(String fileName) {
		try {
			File file = new File(fileName);
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				// To read the current line and process it
				String line = scanner.nextLine();

				// If the line is not empty
				if (line.length() != 0) {
					// To process the line read
					processLine(line, fileName);
				}
			}
			scanner.close();
		}
		// If the csv file is not found, normal exit the system
		catch (FileNotFoundException fnf) {
			String message = fileName
					+ " not found.Cannot process the data from the file. Please rename the file to \"Vehicles.csv\"\n";
			LogManager.getLogger().severe(message);
			System.exit(0);
		}
	}

	// To process the line read from the file
	public void processLine(String line, String fileName) {
		try {
			// Split the line when ',' is read and store it in an array
			String parts[] = line.split(",");

			/*
			 * To process segment number and make the alphabet upper case To remove line
			 * breaks from the first line. The first line read from the file has a line
			 * break by default which needs to be removed in order to achieve the length of
			 * the segment as 2 instead of 3
			 */
			String segment = parts[0].trim().replace("\uFEFF", "");
			segment = segment.toUpperCase();

			// To process the plate number
			String plateNumber = parts[1].trim();
			// To make all alphabets in the number plate uppercase
			plateNumber = plateNumber.toUpperCase();

			// To process the vehicle type
			String type = parts[2].trim();

			// To process the crossing time
			String scrossTime = parts[3].trim();
			// To convert crossing time to int
			int crossTime = Integer.parseInt(scrossTime);

			// To process the direction
			String direction = parts[4].trim();

			// To process the status
			String status = parts[5].trim();

			// To process the length
			String slength = parts[6].trim();
			// To convert String length to int length
			int length = Integer.parseInt(slength);

			// To set emission to zero (temporary)
			int emission = 0;

			boolean ok = false;

			// To create Vehicle object, validate its attributes and then add it to the list
			// if it does not exist already
			Vehicle v = new Vehicle(plateNumber, type, crossTime, direction, length, emission, status, segment);
			// To set emission according to the type
			v.setEmission(type);

			// If the platenumber has a length of 6 AND the first character of the plate
			// number is an alphabet AND the rest of the characters are numbers AND
			if (plateNumber.length() == 6 && Character.isLetter(plateNumber.charAt(0))
					&& plateNumber.substring(1).matches("[0-9]+")
					// If the direction is straight, left or right AND
					&& (direction.equalsIgnoreCase("Straight") || direction.equalsIgnoreCase("Left")
							|| direction.equalsIgnoreCase("Right"))
					// If the type is car, truck or bus AND
					&& (type.equalsIgnoreCase("Car") || type.equalsIgnoreCase("Truck") || type.equalsIgnoreCase("Bus"))
					// If the segment is s1, s2, s3 or s4 AND
					&& (segment.equals("S1") || segment.equals("S2") || segment.equals("S3") || segment.equals("S4"))
					// If the status is waiting or crossed
					&& (status.equalsIgnoreCase("Waiting") || status.equalsIgnoreCase("Crossed")) && crossTime > 0
					&& crossTime <= 15 && length > 0 && length <= 15) {
				// To call the method for adding vehicle
				ok = addOneVehicle(v);
				// If ok is false
				if (ok == false) {
					// Vehicle already exists
					String message = "The vehicle with the Plate Number " + plateNumber + " already exists.\n";
					LogManager.getLogger().info(message);
				}
				addToQueue(v);
			}
			// If the data is incorrect
			else {
				String message = "The vehicle with the Plate Number " + plateNumber
						+ " does not match the required format.\n";
				LogManager.getLogger().info(message);
			}
		}
		// Exceptions for Process Line
		// If the data type is incorrect
		catch (NumberFormatException nfe) {
			String message = "Number conversion error in the line: " + line + " - " + nfe.getMessage() +'\n';
			LogManager.getLogger().info(message);
		}
		// If there are missing values in a line
		catch (ArrayIndexOutOfBoundsException aib) {
			String message = "Not enough items in the line: " + line + " - at index position: " + aib.getMessage() + "\n";
			LogManager.getLogger().info(message);
		}
	}
}