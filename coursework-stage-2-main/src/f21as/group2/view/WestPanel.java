package f21as.group2.view;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import f21as.group2.controller.GUIRenderer;
import f21as.group2.controller.SignalController;
import f21as.group2.interfaces.Observer;

// For the vehicles table
public class WestPanel extends JPanel implements Observer {
	private static final long serialVersionUID = 1L;

	// GUI components
	JTable vehicleTable;
	Font f, headerFont, tableFont;
	private static GUIRenderer guiRenderer;

	// Constructor to create the west panel
	public WestPanel(SignalController sc) {

		// Table models for all the tables in the GUI
		guiRenderer = new GUIRenderer();

		// To register WestPanel as an observer
		sc.registerObserver(this);

		// To initialise font style and font size
		headerFont = new Font("Arial", Font.BOLD, 12);
		tableFont = new Font("Arial", Font.PLAIN, 12);

		// To add scrollbar to the table and add it the content pane
		this.add(new JScrollPane(setVehiclesection()));
	}

	private JTable setVehiclesection() {
		// To create the display section JTable
		vehicleTable = new JTable(guiRenderer.getVtm());

		// Get the table header
		JTableHeader tableHeader = vehicleTable.getTableHeader();
		// Set Font Size
		vehicleTable.setFont(tableFont);
		// To set the size
		vehicleTable.setPreferredScrollableViewportSize(new Dimension(570, 250));

		// Adjust the table column widths and fonts
		TableColumn[] columns = new TableColumn[vehicleTable.getColumnCount()];
		int[] columnWidths = { 55, 50, 85, 70, 50, 50, 50, 50 };
		for (int i = 0; i < columns.length; i++) {
			columns[i] = vehicleTable.getColumnModel().getColumn(i);
			columns[i].setPreferredWidth(columnWidths[i]);
			tableHeader.setFont(headerFont);
		}

		// Change the header row names
		columns[0].setHeaderValue("Vehicle");
		columns[1].setHeaderValue("Type");
		columns[2].setHeaderValue("Crossing Time");
		columns[3].setHeaderValue("Direction");
		columns[4].setHeaderValue("Length");
		columns[5].setHeaderValue("Emission");
		columns[6].setHeaderValue("Status");
		columns[7].setHeaderValue("Segment");

		// To add title
		String title1 = "Vehicle Data";
		Border borderVehicle = BorderFactory.createTitledBorder(title1);
		this.setBorder(borderVehicle);

		return vehicleTable;
	}

	@Override
	// To update the gui when a vehicle gets added
	public void update() {
		// To update the statistics in the GUI
		guiRenderer.getStm().fireTableDataChanged();
		// To update the vehicles in the GUI
		guiRenderer.getVtm().fireTableDataChanged();

		// To update the co2 in the GUI
		EastPanel.co2.setText(String.format("%.2f", guiRenderer.getiCollection().totalEmissions()));
	}
}
