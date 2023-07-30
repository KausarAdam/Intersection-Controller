package f21as.group2.model;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

// Model for the vehicle table created for the GUI
public class VehicleTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private ArrayList<Vehicle> vehicleList;

	public VehicleTableModel(IntersectionCollection data) {
		vehicleList = data.getVehicleList();
	}

	@Override
	public int getRowCount() {
		return vehicleList.size();
	}

	@Override
	public int getColumnCount() {
		return 8;
	}

	@Override
	public String getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex == 0) {
			// Vehicle Plate Number
			return vehicleList.get(rowIndex).getPlateNumber();
		} else if (columnIndex == 1) {
			// Vehicle Type
			return vehicleList.get(rowIndex).getType();
		} else if (columnIndex == 2) {
			// Cross Time
			String ct = "";
			return ct += vehicleList.get(rowIndex).getCrossTime();
		} else if (columnIndex == 3) {
			// Direction
			String gd = "";
			return gd += vehicleList.get(rowIndex).getDirection();
		} else if (columnIndex == 4) {
			// Length
			String gl = "";
			return gl += vehicleList.get(rowIndex).getLength();
		} else if (columnIndex == 5) {
			// Emission
			String ge = "";
			return ge += vehicleList.get(rowIndex).getEmission();
		} else if (columnIndex == 6) {
			// Crossing Status
			return vehicleList.get(rowIndex).getStatus();
		} else if (columnIndex == 7) {
			// Segment
			return vehicleList.get(rowIndex).getSegment();
		}
		return null;
	}
}
