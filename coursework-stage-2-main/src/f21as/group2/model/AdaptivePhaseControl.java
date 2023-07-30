package f21as.group2.model;

import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;

/* This class is for the extended functionality. 
 * It sets the durations of the phases according to how many vehicles are there in the phase
 */
public class AdaptivePhaseControl {
	// Instance variables
	private int originalDuration;
	private ArrayList<Integer> originalCrossingtime;

	// To add parameter and save arraylist of crossingtime
	public AdaptivePhaseControl(int originalDuration, ArrayList<Integer> originalCrossingtime) {
		this.originalDuration = originalDuration;
		this.originalCrossingtime = originalCrossingtime;
	}

	// To set the durations at the end of each cycle
	public void sortForNextCycle(TreeMap<String, Integer> adaptiveTable, ArrayList<Integer> crossingTime) {
		int index = 0;
		/*
		 * Counter is used to check the difference of total time between current cycle
		 * and total time of original cycle in order to decide whether it's necessary to
		 * apply the original timer to every phase or implement new optimized timer
		 */
		int counter = 0;

		// To update the list
		for (int i = 0; i < 8; i++) {
			counter = counter + crossingTime.get(i);
		}

		counter = counter - originalDuration;

		if (counter > 0) {
			/*
			 * Logic: Using the original total time with the new total time, we will use the
			 * difference between the two times and try to reduce the new time as much as
			 * possible towards the original time so that there is no ridiculously high
			 * phase timer on any cycle.
			 * 
			 * Pseudocode: If there is a positive difference between the two, then firstly
			 * find 8 equal portions of the difference and subtract from them. The remainder
			 * from the portion will be added to any phase at random. In the end, the equal
			 * portion value will be used to subtract from the new phase timer
			 */

			// To get the remainder
			int rem = counter % 8;

			// To deduct the remainder from a phase randomly
			Random r = new Random();
			int num = r.nextInt(8) + 0;
			crossingTime.set(num, crossingTime.get(num) - rem);

			// To get the dispersed value by dividing it by 8 (total number of phases)
			int disperseVal = (counter - rem) / 8;

			index = 0;
			for (Integer integer : crossingTime) {
				// Newly assumed phase timer
				integer = integer - disperseVal;

				// To limit the phase timer between 15 and 60
				int newCrossingTime = (crossingTime.get(index) - disperseVal);
				if (newCrossingTime > 60)
					newCrossingTime = 60;
				else if (newCrossingTime < 15)
					newCrossingTime = originalCrossingtime.get(index);

				// To replace the duration with the new value
				adaptiveTable.replace("P" + (index + 1), newCrossingTime);
				index++;
			}
		} else {
			// If the counter is negative or 0, retrieve the defualt phase durations
			index = 0;
			for (Integer integer : originalCrossingtime) {
				adaptiveTable.replace("P" + (index + 1), integer);
				index++;
			}
		}
	}
}
