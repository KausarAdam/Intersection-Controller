package f21as.group2.controller;

import f21as.group2.interfaces.Observer;
import f21as.group2.interfaces.Subject;
import f21as.group2.model.*;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/* 
 * To control the activation of phases (signals)
 * This class implements the subject interface since SignalController is responsible 
 * for managing the phase signals every second and it needs to notify the observers 
 * about this change so that they can update themselves
 * */
public class SignalController extends Thread implements Subject {

	// To create a boolean array of 8 elements (each index representing a phase
	// number)
	private static boolean[] phaseOn;
	private static Phase p;
	private static int i = 0;

	// List to hold any observers
	private List<Observer> registeredObservers;

	// Thread service
	private ScheduledExecutorService ses;

	// To read the intersection file
	public SignalController(Phase existingPhase) {
		ses = Executors.newScheduledThreadPool(1);
		p = existingPhase;
		phaseOn = new boolean[8];
		registeredObservers = new LinkedList<Observer>();
	}

	// Empty constructor
	public SignalController() {
	}

	@Override
	// To start the timer and keep operating the signals
	public void run() {
		Runnable scRun = () -> {
			if (i <= p.getTotalDuration()) {
				changePhaseStatus();

				i++;

				// To reset the timer to 1 after the last phase is over
				if (i - 1 == p.getTotalUpto(8)) {
					// To call the extended funcationality at the end of each cycle
					IntersectionCollection.runAdaptivePolicy();
					i = 1;
				}
			}
			// To notify observers so that they refresh themselves
			notifyObservers();
		};
		ses.scheduleAtFixedRate(scRun, 2, 1, TimeUnit.SECONDS);
	}

	// To change the boolean value of the ongoing phase to true
	public void changeBool(int pNum) {
		// To change the status of all phases to false except the passed phase pNum
		for (int c = 0; c < phaseOn.length; c++) {
			if (c != pNum - 1) {
				phaseOn[c] = false;
			}
		}
		// To change the status of the passed phase to true
		if (phaseOn[pNum - 1] == false) {
			phaseOn[pNum - 1] = true;
			notifyObservers();
			String message = "The status of P" + pNum + " changed to true.\n";
			LogManager.getLogger().info(message);
		}
	}

	// To change the status of the phases according to the timer 'i'
	public void changePhaseStatus() {
		if (i < p.getValue("P1")) {
			changeBool(1);
		} else if (i == p.getTotalUpto(1)) {
			changeBool(2);
		} else if (i == p.getTotalUpto(2)) {
			changeBool(3);
		} else if (i == p.getTotalUpto(3)) {
			changeBool(4);
		} else if (i == p.getTotalUpto(4)) {
			changeBool(5);
		} else if (i == p.getTotalUpto(5)) {
			changeBool(6);
		} else if (i == p.getTotalUpto(6)) {
			changeBool(7);
		} else if (i == p.getTotalUpto(7)) {
			changeBool(8);
		}
	}

	// To find the remaining time of the phase
	public static int getRemainingTime() {
		int result = 0;

		int phaseCount1 = p.getTotalUpto(1);
		int phaseCount2 = p.getTotalUpto(2);
		int phaseCount3 = p.getTotalUpto(3);
		int phaseCount4 = p.getTotalUpto(4);
		int phaseCount5 = p.getTotalUpto(5);
		int phaseCount6 = p.getTotalUpto(6);
		int phaseCount7 = p.getTotalUpto(7);
		int phaseCount8 = p.getTotalUpto(8);

		if (i - 1 < phaseCount1) {
			result = phaseCount1 - i;
		} else if (i - 1 < phaseCount2) {
			result = phaseCount2 - i;
		} else if (i - 1 < phaseCount3) {
			result = phaseCount3 - i;
		} else if (i - 1 < phaseCount4) {
			result = phaseCount4 - i;
		} else if (i - 1 < phaseCount5) {
			result = phaseCount5 - i;
		} else if (i - 1 < phaseCount6) {
			result = phaseCount6 - i;
		} else if (i - 1 < phaseCount7) {
			result = phaseCount7 - i;
		} else if (i - 1 < phaseCount8) {
			result = phaseCount8 - i;
		}
		return result;
	}

	// To check which phase is running currently 'i'
	public static int getTruePhase() {
		for (int j = 0; j < phaseOn.length; j++) {
			if (phaseOn[j])
				return j;
		}
		return 0;
	}

	// To get the phase number of the activated phase as string
	public static String getActivatedPhaseNumber() {
		int i = getTruePhase();
		if (i == 0) {
			return "P1";
		} else if (i == 1) {
			return "P2";
		} else if (i == 2) {
			return "P3";
		} else if (i == 3) {
			return "P4";
		} else if (i == 4) {
			return "P5";
		} else if (i == 5) {
			return "P6";
		} else if (i == 6) {
			return "P7";
		} else {
			return "P8";
		}
	}

	@Override
	// To add observer to the list
	public void registerObserver(Observer obs) {
		registeredObservers.add(obs);
	}

	@Override
	// To remove observer to the list
	public void removeObserver(Observer obs) {
		registeredObservers.remove(obs);
	}

	@Override
	// To notify observers so that they update themselves
	public void notifyObservers() {
		for (Observer obs : registeredObservers)
			obs.update();
	}
}
