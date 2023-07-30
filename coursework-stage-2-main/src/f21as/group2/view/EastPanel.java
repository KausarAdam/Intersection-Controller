package f21as.group2.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import f21as.group2.controller.GUIRenderer;
import f21as.group2.controller.SignalController;
import f21as.group2.controller.TextLimiter;
import f21as.group2.interfaces.Observer;
import f21as.group2.model.IntersectionCollection;

// For the stats and phase table. Also contains co2, total duration and timer
public class EastPanel extends JPanel implements Observer {
	private static final long serialVersionUID = 1L;

	// GUI components
	JTable statsTable;
	static JTable phaseTable;
	static JTextField co2, totalDuration, pTime, avgWaiting;
	Font f, headerFont, tableFont;
	private static GUIRenderer guiRenderer;

	// To create the east panel
	public EastPanel(SignalController sc) {
		
		guiRenderer = new GUIRenderer();
		co2 = new JTextField();
		totalDuration = new JTextField();
		pTime = new JTextField();
		avgWaiting = new JTextField();

		// To register EastPanel as an observer
		sc.registerObserver(this);

		// To set the layout of this panel to border layout
		this.setLayout(new BorderLayout(2, 2));

		// To initialise font style and font size
		f = new Font(Font.SANS_SERIF, Font.PLAIN, 17);
		headerFont = new Font("Arial", Font.BOLD, 12);
		tableFont = new Font("Arial", Font.PLAIN, 12);

		// To create the final section and add it to the content pane
		this.add(setPhaseSection(), BorderLayout.WEST);
		this.add(setStatsSection(), BorderLayout.EAST);
		this.add(setTimerAndEmissionSection(), BorderLayout.SOUTH);

		update();
	}

	// Getters and setters
	public static JTable getPhaseTable() {
		return phaseTable;
	}

	public static JTextField getCo2() {
		return co2;
	}

	public static void setCo2(JTextField co2) {
		EastPanel.co2 = co2;
	}

	private JPanel setPhaseSection() {
		// To display the phases
		JPanel phase = new JPanel();
		phaseTable = new JTable(guiRenderer.getPtm());
		// Get the table header
		JTableHeader tableHeader = phaseTable.getTableHeader();
		// Set Font Size
		phaseTable.setFont(tableFont);
		// Set the JTable and JScrollPane size to be the same
		phaseTable.setPreferredScrollableViewportSize(phaseTable.getPreferredSize());

		// To add title
		String title2 = "Phase Data";
		Border borderPhase = BorderFactory.createTitledBorder(title2);

		// Adjust the table column widths and fonts
		TableColumn[] columns = new TableColumn[phaseTable.getColumnCount()];
		int[] columnWidths = { 50, 50 };
		for (int i = 0; i < columns.length; i++) {
			columns[i] = phaseTable.getColumnModel().getColumn(i);
			columns[i].setPreferredWidth(columnWidths[i]);
			tableHeader.setFont(headerFont);
		}

		// Change the header row names
		columns[0].setHeaderValue("Phase");
		columns[1].setHeaderValue("Duration (s)");

		// To set the border of the Table
		phase.setBorder(borderPhase);
		phase.add(new JScrollPane(phaseTable));

		return phase;
	}

	private JPanel setStatsSection() {
		// To create the display section JTable
		statsTable = new JTable(guiRenderer.getStm());

		// Get the table header
		JTableHeader statsTableHeader = statsTable.getTableHeader();
		// Set Font Size
		statsTable.setFont(tableFont);
		// To set the size
		statsTable.setPreferredScrollableViewportSize(new Dimension(360, 64));

		// Adjust the table column widths and fonts
		TableColumn[] statColumns = new TableColumn[statsTable.getColumnCount()];
		int[] statColumnWidths = { 10, 2, 25, 45, 37 };
		for (int i = 0; i < statColumns.length; i++) {
			statColumns[i] = statsTable.getColumnModel().getColumn(i);
			statColumns[i].setPreferredWidth(statColumnWidths[i]);
			statsTableHeader.setFont(headerFont);
		}

		// Change the header row names
		statColumns[0].setHeaderValue("Segment");
		statColumns[1].setHeaderValue("Vehicles");
		statColumns[2].setHeaderValue("Length (m)");
		statColumns[3].setHeaderValue("Cross Time (s)");
		statColumns[4].setHeaderValue("Wait Time (s)");

		String title3 = "Statistics Data";
		Border borderStats = BorderFactory.createTitledBorder(title3);
		// To make the table non-editable
		statsTable.setDefaultEditor(Object.class, null);

		// Set Font Size
		statsTable.setFont(tableFont);

		// Get the table header and set font
		JTableHeader tableHeader1 = statsTable.getTableHeader();
		tableHeader1.setFont(headerFont);

		// To display statistics with heading
		JPanel statistic = new JPanel();
		statistic.setBorder(borderStats);
		statistic.add(new JScrollPane(statsTable));

		return statistic;
	}

	private JPanel setTimerAndEmissionSection() {
		// To add the emission field
		JPanel emission = new JPanel(new FlowLayout());
		// To use subscript on '2' in CO2 using unicode
		emission.add(createOneLabel("CO\u2082 ", JLabel.RIGHT));
		// To set the colour of the non-editable field to white
		UIManager.put("TextField.inactiveBackground", Color.WHITE);
		co2.setText(String.format("%.2f", guiRenderer.getiCollection().totalEmissions()));
		// To increase the font size of co2
		co2.setFont(f);
		co2.setEditable(false);
		co2.setColumns(5);
		co2.setDocument(new TextLimiter(6));
		emission.add(co2);
		emission.add(createOneLabel("grams", JLabel.LEFT));
		emission.add(createOneLabel("        Avg Waiting Time", JLabel.LEFT));
		avgWaiting.setText(String.format("%.2f", guiRenderer.getiCollection().avgWaitingTime()));
		avgWaiting.setFont(f);
		avgWaiting.setEditable(false);
		avgWaiting.setColumns(5);
		emission.add(avgWaiting);
		emission.add(createOneLabel("s", JLabel.LEFT));

		// To add the timers
		JPanel timer = new JPanel(new FlowLayout());
		timer.add(createOneLabel("Total Duration", JLabel.LEFT));
		totalDuration.setEditable(false);
		totalDuration.setFont(f);
		totalDuration.setColumns(3);
		totalDuration.setText(Integer.toString(IntersectionCollection.getExistingPhase().getTotalDuration()));
		timer.add(totalDuration);
		timer.add(createOneLabel("s", JLabel.LEFT));
		timer.add(createOneLabel("        Phase Timer", JLabel.RIGHT));
		pTime.setEditable(false);
		pTime.setFont(f);
		pTime.setColumns(2);
		timer.add(pTime);
		timer.add(createOneLabel("s", JLabel.LEFT));

		// To combine timer and emission panels
		JPanel timerAndEmission = new JPanel(new BorderLayout());
		timerAndEmission.add(emission, BorderLayout.NORTH);
		timerAndEmission.add(timer, BorderLayout.SOUTH);

		return timerAndEmission;
	}

	// To create labels with different strings and alignment
	private JLabel createOneLabel(String s, int align) {
		JLabel label = new JLabel(s, align);
		label.setForeground(Color.BLACK);
		label.setFont(f);
		return label;
	}

	@Override
	// To update the contents of the east panel
	public void update() {
		if (phaseTable == null)
			return;

		// To update the phase table in the GUI
		guiRenderer.getPtm().fireTableDataChanged();

		// To update the co2 in the GUI
		co2.setText(String.format("%.2f", guiRenderer.getiCollection().totalEmissions()));
		
		avgWaiting.setText(String.format("%.2f", guiRenderer.getiCollection().avgWaitingTime()));

		if (pTime != null) {
			pTime.setText(Integer.toString(SignalController.getRemainingTime()));
			totalDuration.setText(Integer.toString(IntersectionCollection.getExistingPhase().getTotalDuration()));
		}

		// To change the phases colour according to the activated phase
		phaseTable.setDefaultRenderer(Object.class,
				new MyTableRenderer(guiRenderer.getiCollection().getTruePhaseFromCentralController(),
						guiRenderer.getiCollection().getRemainingTime()));
	}

}
