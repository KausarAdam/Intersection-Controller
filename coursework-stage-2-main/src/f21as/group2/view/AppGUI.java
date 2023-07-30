package f21as.group2.view;

import f21as.group2.controller.*;
import f21as.group2.model.IntersectionCollection;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

// Creates the GUI for the simulator
public class AppGUI extends JFrame {
	private static final long serialVersionUID = 1L;

	// GUI components
	ScheduledExecutorService ses;

	// Objects
	private static GUIRenderer guiRenderer;
	private SignalController sc;
	private CentralController cc;

	// Constructor
	public AppGUI() {
		guiRenderer = new GUIRenderer();
		guiRenderer.initialise();

		ses = Executors.newScheduledThreadPool(1);

		sc = new SignalController(IntersectionCollection.getExistingPhase());
		cc = new CentralController(guiRenderer.getiCollection().getQueues(SignalController.getActivatedPhaseNumber()));

		// To start threads for signal controller and central controller
		Thread t1 = new Thread(cc);
		Thread t2 = new Thread(sc);

		// To update the vehicle data
		Runnable executeProcess = () -> {
			cc.updateVehicleData(guiRenderer.getiCollection().getQueues("P1"),
					guiRenderer.getiCollection().getQueues("P2"), guiRenderer.getiCollection().getQueues("P3"),
					guiRenderer.getiCollection().getQueues("P4"), guiRenderer.getiCollection().getQueues("P5"),
					guiRenderer.getiCollection().getQueues("P6"), guiRenderer.getiCollection().getQueues("P7"),
					guiRenderer.getiCollection().getQueues("P8"));
		};

		// To update the queues every second
		ses.scheduleAtFixedRate(executeProcess, 1, 1, TimeUnit.SECONDS);
		t1.start();
		t2.start();

		// To set the size of the frame
		setSize(1185, 500);
		// To position the frame in the centre
		setLocationRelativeTo(null);
		// To prevent user from resizing the frame
		setResizable(false);

		// To set the title and icon of the window
		setTitle(" Group 2 - Traffic Simulator");

		// To call the panels of the frame
		this.add(new EastPanel(sc), BorderLayout.EAST);
		this.add(new WestPanel(sc), BorderLayout.WEST);
		this.add(new SouthPanel(sc), BorderLayout.SOUTH);

		// To disable the standard close button
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		// To generate vehicles automatically
		guiRenderer.getiCollection().vehicleAutoGenerator();

	}

}
