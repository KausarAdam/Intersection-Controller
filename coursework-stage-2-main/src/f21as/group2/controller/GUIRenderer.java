package f21as.group2.controller;

import f21as.group2.model.*;

// An intermediate class between the view and controllers
public class GUIRenderer {

	// Intersection Collection Instance Variable
	private static IntersectionCollection iCollection;
	// Data Models for JTables
	private static VehicleTableModel vtm;
	private static PhaseTableModel ptm;
	private static StatsTableModel stm;

	// Constructors
	public GUIRenderer() {
	}

	public void initialise() {
		// To read and process the text files
		iCollection = new IntersectionCollection();
		iCollection.readFile("Vehicles.csv");
		// To run the extended functionality at the beginning
		IntersectionCollection.runAdaptivePolicy();

		// Table models for all the tables in the GUI
		vtm = new VehicleTableModel(iCollection);
		ptm = new PhaseTableModel();
		stm = new StatsTableModel(iCollection);
	}

	// Getters and setters
	public IntersectionCollection getiCollection() {
		return iCollection;
	}

	public void setiCollection(IntersectionCollection iCollection) {
		GUIRenderer.iCollection = iCollection;
	}

	public VehicleTableModel getVtm() {
		return vtm;
	}

	public void setVtm(VehicleTableModel vtm) {
		GUIRenderer.vtm = vtm;
	}

	public PhaseTableModel getPtm() {
		return ptm;
	}

	public void setPtm(PhaseTableModel ptm) {
		GUIRenderer.ptm = ptm;
	}

	public StatsTableModel getStm() {
		return stm;
	}

	public void setStm(StatsTableModel stm) {
		GUIRenderer.stm = stm;
	}

	// To generate vehicle automatically
	public Vehicle generateVehicle(String vNum, String vType, int vCross, String vDirection, int vLength, int vEmission,
			String vStatus, String vSegment) {
		Vehicle v = new Vehicle(vNum, vType, vCross, vDirection, vLength, vEmission, vStatus, vSegment);
		return v;
	}

}
