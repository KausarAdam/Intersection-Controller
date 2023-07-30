package f21as.group2.main;

import f21as.group2.view.*;
import f21as.group2.controller.*;

// Manages the program by starting the GUI
public class Manager {
	private AppGUI vehicleGUI;

	// Constructor
	public Manager() throws DuplicateIDException {
		this.vehicleGUI = new AppGUI();
	}

	public void run() {
		// Set visibility to true upon running
		vehicleGUI.setVisible(true);
	}
}
