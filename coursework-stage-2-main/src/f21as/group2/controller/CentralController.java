package f21as.group2.controller;

import f21as.group2.model.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

// To control the simulator and change the status of vehicles
public class CentralController extends Thread {

	// Variables
	private int recordTimer;
	private String recordPhase;

	private boolean checkCross;
	private static int orangeDuration;
	private Queue<Vehicle> currentQueue, processVehicle1, processVehicle2, processVehicle3, processVehicle4,
			processVehicle5, processVehicle6, processVehicle7, processVehicle8;
	ScheduledExecutorService ses, ses2;

	// Constructor
	public CentralController(Queue<Vehicle> initialQueue) {
		currentQueue = initialQueue;
		checkCross = false;
		orangeDuration = 1;
		ses = Executors.newScheduledThreadPool(1);
		ses2 = Executors.newScheduledThreadPool(1);
		recordPhase = "";
	}

	@Override
	// To run executeVehicle()
	public synchronized void run() {
		Runnable csRun = () -> {
			executeVehicle();
			
		};
		ses.scheduleWithFixedDelay(csRun, 2, 1, TimeUnit.SECONDS);
	}

	// To dequeue the vehicle and change status to crossed
	// To make the vehicles cross according to the remaining time left for the phase
	public synchronized void executeVehicle() {
		try {
			// To get the queue that is being used by the current phase and the remainder
			String checkPhase = SignalController.getActivatedPhaseNumber();
			int remainder = SignalController.getRemainingTime();

			/*
			 * This is done to record the current phase and the remainder The first if
			 * condition will run at the start of the program (initially) The logic here is
			 * that the system timer must be less than the record timer because the system
			 * timer is more recent. When an intersection cycle ends, there is a possibilty
			 * that the timers sync out. To solve this issue, we will keep checking the
			 * condition to ensure that the system timer is always smaller than the record
			 * timer for the same phase. If the system timer is greater than the record
			 * timer, this cycle of "executeVehicle" will be stopped
			 */
			if (recordPhase == "") {
				recordPhase = checkPhase;
				recordTimer = remainder;
			} else if (recordPhase != checkPhase) {
				recordPhase = checkPhase;
				recordTimer = remainder;
			} else if (checkPhase == recordPhase && remainder > recordTimer) {
				return;
			}

			if (checkPhase == "P1") {
				currentQueue = processVehicle1;
			} else if (checkPhase == "P2") {
				currentQueue = processVehicle2;
			} else if (checkPhase == "P3") {
				currentQueue = processVehicle3;
			} else if (checkPhase == "P4") {
				currentQueue = processVehicle4;
			} else if (checkPhase == "P5") {
				currentQueue = processVehicle5;
			} else if (checkPhase == "P6") {
				currentQueue = processVehicle6;
			} else if (checkPhase == "P7") {
				currentQueue = processVehicle7;
			} else if (checkPhase == "P8") {
				currentQueue = processVehicle8;
			}

			// If there are 0 vehicles
			if (currentQueue.size() == 0)
				checkCross = false;
			// If there are more than 0 vehicles
			if (currentQueue.size() > 0) {
				Vehicle v = currentQueue.peek();

				// Do nothing if the vehicle status is crossed, else check whether the remaining
				// time of the phase is enough for the vehicle to cross and the vehicle is at
				// the intersection
				if (v.getStatus().equals("Crossed")) {
				} else if (remainder >= orangeDuration && remainder + 1 >= v.getCrossTime()) {
					// If checkCross is false (intersection is free)
					if (checkCross == false) {
						String message = v.getPlateNumber() + " in " + v.getSegment() + " starts crossing here to go "
								+ v.getDirection() + " with crossing time of " + v.getCrossTime() + "\n";
						LogManager.getLogger().info(message);
						checkCross = true;

						// This runnable will change the status of checkCross once the vehicle has
						// crossed the intersection
						Thread veh = v;
						veh.start();

						Runnable test = () -> {
							checkCross = false;
							currentQueue.remove();
						};

						// Deducted one here since 0 is counted as well
						ses2.schedule(test, v.getCrossTime(), TimeUnit.SECONDS);
					}
				}
			}
			recordTimer--;
		} catch (NoSuchElementException e) {
			// To convert stack trace to string
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			String message = sw.toString() + "\n";
			// Severe because it can affect the normal functioning of the program
			LogManager.getLogger().severe(message);
		}
	}

	// Getter and setter
	public static int getOrangeDuration() {
		return orangeDuration;
	}

	// To get the current phase
	public int getPhaseStatus() {
		return SignalController.getTruePhase();
	}

	// To update the data of the queues
	public void updateVehicleData(Queue<Vehicle> data1, Queue<Vehicle> data2, Queue<Vehicle> data3,
			Queue<Vehicle> data4, Queue<Vehicle> data5, Queue<Vehicle> data6, Queue<Vehicle> data7,
			Queue<Vehicle> data8) {
		processVehicle1 = data1;
		processVehicle2 = data2;
		processVehicle3 = data3;
		processVehicle4 = data4;
		processVehicle5 = data5;
		processVehicle6 = data6;
		processVehicle7 = data7;
		processVehicle8 = data8;
	}

}