package f21as.group2.model;

import javax.swing.table.AbstractTableModel;

// Model for the phase table created for the GUI
public class PhaseTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private Phase phaseData;

	public PhaseTableModel() {
		// To load the data in phaseData else we will get an error
		phaseData = IntersectionCollection.getExistingPhase();
	}

	@Override
	public int getRowCount() {
		return 8;
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public String getValueAt(int rowIndex, int columnIndex) {
		// Key for Phase Tree Map
		String key = (String) phaseData.getPhaseTable().keySet().toArray()[rowIndex];
		if (columnIndex == 0) {
			// Returns Key Value for Phases
			return key;
		} else if (columnIndex == 1) {
			// Returns duration value
			String duration = "";
			return duration += phaseData.getPhaseTable().get(key);
		}
		return null;
	}

}
