package f21as.group2.model;

import javax.swing.table.AbstractTableModel;

// Table model for displaying statistics on the gui
public class StatsTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private IntersectionCollection ic;

	public StatsTableModel(IntersectionCollection pass) {
		ic = pass;
	}

	@Override
	public int getRowCount() {
		return 4;
	}

	@Override
	public int getColumnCount() {
		return 5;
	}

	@Override
	public String getValueAt(int r, int c) {
		String value = "";
		// For the first column -> Segment numbers
		if (c == 0) {
			if (r == 0) {
				value = "S1";
			} else if (r == 1) {
				value = "S2";
			} else if (r == 2) {
				value = "S3";
			} else {
				value = "S4";
			}
		}
		// For the second column -> Number of vehicles waiting to cross
		else if (c == 1) {
			if (r == 0) {
				value = String.valueOf(ic.waitingToCrossPerSeg("S1"));
			} else if (r == 1) {
				value = String.valueOf(ic.waitingToCrossPerSeg("S2"));
			} else if (r == 2) {
				value = String.valueOf(ic.waitingToCrossPerSeg("S3"));
			} else {
				value = String.valueOf(ic.waitingToCrossPerSeg("S4"));
			}
		}
		// For the third column -> Length of vehicle queues in segments
		else if (c == 2) {
			if (r == 0) {
				value = String.valueOf(ic.totalLengthPerSeg("S1"));
			} else if (r == 1) {
				value = String.valueOf(ic.totalLengthPerSeg("S2"));
			} else if (r == 2) {
				value = String.valueOf(ic.totalLengthPerSeg("S3"));
			} else {
				value = String.valueOf(ic.totalLengthPerSeg("S4"));
			}
		}
		// For the 4th column -> Crossing time for each segment
		else if (c == 3) {
			if (r == 0) {
				value = String.valueOf(ic.avgCrossTimePerSeg("S1"));
			} else if (r == 1) {
				value = String.valueOf(ic.avgCrossTimePerSeg("S2"));
			} else if (r == 2) {
				value = String.valueOf(ic.avgCrossTimePerSeg("S3"));
			} else {
				value = String.valueOf(ic.avgCrossTimePerSeg("S4"));
			}
		}
		// For the 5th column -> Waiting time for each segment
		else if (c == 4) {
			if (r == 0) {
				value = String.valueOf(ic.totalWaitPerSeg("S1"));
			} else if (r == 1) {
				value = String.valueOf(ic.totalWaitPerSeg("S2"));
			} else if (r == 2) {
				value = String.valueOf(ic.totalWaitPerSeg("S3"));
			} else {
				value = String.valueOf(ic.totalWaitPerSeg("S4"));
			}
		}
		return value;
	}
}
