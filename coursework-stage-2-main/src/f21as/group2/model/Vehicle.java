package f21as.group2.model;

import f21as.group2.controller.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

// Stores all the vehicle data
public class Vehicle extends Thread {
	// Instance Variables
	private String plateNumber;
	private String type;
	private int crossTime;
	private String direction;
	private int length;
	private int emission;
	private String status;
	private String segment;

	ScheduledExecutorService ses;

	// Constructor
	public Vehicle(String pn, String type, int ct, String direction, int l, int e, String status, String segment) {
		this.plateNumber = pn;
		this.type = type;
		this.crossTime = ct;
		this.direction = direction;
		this.length = l;
		this.emission = e;
		this.status = status;
		this.segment = segment;
		ses = Executors.newScheduledThreadPool(1);
	}

	// For crossing vehicles
	@Override
	public void run() {
		Runnable execute = () -> {
			executeDuration();
		};
		int timerr = crossTime - 2;
		ses.schedule(execute, timerr, TimeUnit.SECONDS);

	}

	// To change the status of vehicles after they cross and to refresh gui
	public void executeDuration() {
		String message = "Crossed! Plate: " + plateNumber + ", Cross time: " + crossTime + ", Satus: " + status + "\n";
		LogManager.getLogger().info(message);
		this.setStatus("Crossed");
		try {
		} catch (NullPointerException e) {
			// To convert stack trace to string
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			String message2 = sw.toString() + "\n";
			// Severe because it can affect the normal functioning of the program
			LogManager.getLogger().severe(message2);
		}
	}

	// Getters & Setters
	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getCrossTime() {
		return crossTime;
	}

	public void setCrossTime(int crossTime) {
		this.crossTime = crossTime;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getEmission() {
		return emission;
	}

	// To set emission according to the vehicle type
	public void setEmission(String passType) {
		// If petrol, then emission is 5 per minute
		if (passType.equals("Car")) {
			emission = 5;
		}
		// If diesel, then emission is 10 per minute
		else if (passType.equals("Truck") || passType.equals("Bus")) {
			emission = 10;
		}
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSegment() {
		return segment;
	}

	public void setSegment(String segment) {
		this.segment = segment;
	}

	// To get the details of vehicle (override)
	public String toString() {
		return "Segment: " + segment + ", Plate Number: " + plateNumber + ", Type: " + type + ", Crossing Time: "
				+ crossTime + ", Direction: " + direction + ", Status: " + status + ", Length: " + length
				+ ", Emission per second: " + emission + ".\n";
	}
}
