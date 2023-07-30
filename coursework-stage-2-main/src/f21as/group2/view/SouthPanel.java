package f21as.group2.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import f21as.group2.controller.DuplicateIDException;
import f21as.group2.controller.GUIRenderer;
import f21as.group2.controller.LogManager;
import f21as.group2.controller.SignalController;
import f21as.group2.interfaces.Observer;

// For adding a new vehicle and contains buttons
public class SouthPanel extends JPanel implements ActionListener, Observer {

	private static final long serialVersionUID = 1L;

	// GUI components
	JTextField v, t, ct, d, l, e, s, seg, vuser, ctuser, luser, addMessage, status, autoEmission;
	JButton addButton, cancel, exitButton;
	JComboBox<String> type, direction, emission, segment;
	Font f;
	private static GUIRenderer guiRenderer;

	// Constructor to create the south panel
	public SouthPanel(SignalController sc) {

		guiRenderer = new GUIRenderer();

		// To initialise font style and font size
		f = new Font(Font.SANS_SERIF, Font.PLAIN, 17);

		// To register SouthPanel as an observer
		sc.registerObserver(this);

		// To set the layout of the JPanel to grid layout
		this.setLayout(new GridLayout(3, 1));

		// To enter the add heading
		JPanel header = new JPanel(new GridLayout(2, 1));
		// First Row -> Empty line
		header.add(createOneLabel("", JLabel.LEFT));
		// Second Row -> Heading
		header.add(createOneLabel(" Add Vehicle", JLabel.LEFT));

		// To add the south panel to the content pane
		this.add(header);
		this.add(setAddingVehicleSection());
		this.add(setButtonSection());
	}

	private JPanel setAddingVehicleSection() {
		// To create the textfields for adding vehicles
		JPanel user = new JPanel(new GridLayout(2, 9));
		// First Row
		UIManager.put("TextField.inactiveBackground", Color.LIGHT_GRAY);
		v = new JTextField();
		v.setText("Vehicle");
		v.setEditable(false);
		user.add(v);
		t = new JTextField();
		t.setText("Type");
		t.setEditable(false);
		user.add(t);
		ct = new JTextField();
		ct.setText("Crossing Time");
		ct.setEditable(false);
		user.add(ct);
		d = new JTextField();
		d.setText("Direction");
		d.setEditable(false);
		user.add(d);
		l = new JTextField();
		l.setText("Length");
		l.setEditable(false);
		user.add(l);
		e = new JTextField();
		e.setText("Emission");
		e.setEditable(false);
		user.add(e);
		s = new JTextField();
		s.setText("Status");
		s.setEditable(false);
		user.add(s);
		seg = new JTextField();
		seg.setText("Segment");
		seg.setEditable(false);
		user.add(seg);
		user.add(createOneLabel("", JLabel.LEFT));

		// Second Row
		vuser = new JTextField();
		user.add(vuser);
		String[] tchoices = { "Car", "Bus", "Truck" };
		type = new JComboBox<String>(tchoices);
		user.add(type);
		type.addActionListener(this);
		ctuser = new JTextField();
		user.add(ctuser);
		String[] dchoices = { "Straight", "Right", "Left" };
		direction = new JComboBox<String>(dchoices);
		user.add(direction);
		luser = new JTextField();
		user.add(luser);
		// To set the colour of the noneditable field to white
		UIManager.put("TextField.inactiveBackground", Color.WHITE);
		autoEmission = new JTextField();
		autoEmission.setEditable(false);
		autoEmission.setText("5");
		user.add(autoEmission);
		status = new JTextField();
		status.setText("Waiting");
		status.setEditable(false);
		user.add(status);
		String[] segchoices = { "S1", "S2", "S3", "S4" };
		segment = new JComboBox<String>(segchoices);
		user.add(segment);
		user.add(createOneLabel("", JLabel.LEFT));

		return user;
	}

	private JPanel setButtonSection() {
		// Button Panel
		JPanel buttonPanel = new JPanel(new GridLayout(2, 5, 20, 0));
		// First Row -> Add Message
		UIManager.put("TextField.inactiveBackground", Color.decode("#eeeeee"));
		addMessage = new JTextField();
		addMessage.setEditable(false);
		addMessage.setForeground(Color.RED);
		buttonPanel.add(addMessage);
		for (int i = 0; i < 9; i++) {
			buttonPanel.add(createOneLabel("", JLabel.LEFT));
		}
		// Second row
		addButton = new JButton("Add");
		cancel = new JButton("Cancel");
		exitButton = new JButton("Exit");
		buttonPanel.add(addButton);
		buttonPanel.add(cancel);
		for (int i = 0; i < 2; i++) {
			buttonPanel.add(createOneLabel("", JLabel.LEFT));
		}
		buttonPanel.add(exitButton);
		// To specify the action when a button is pressed
		addButton.addActionListener(this);
		cancel.addActionListener(this);
		exitButton.addActionListener(this);

		return buttonPanel;
	}

	// To create labels with different strings and alignment
	private JLabel createOneLabel(String s, int align) {
		JLabel label = new JLabel(s, align);
		label.setForeground(Color.BLACK);
		label.setFont(f);
		return label;
	}

	// To override method for specifying the actions carried out when a button is
	// clicked
	public void actionPerformed(ActionEvent e) {
		// If exit is clicked, exit the system
		if (e.getSource() == exitButton) {
			// Exit
			System.exit(0);
		}
		// If add is clicked, add the vehicle after validation
		else if (e.getSource() == addButton) {
			try {
				addNewVehicle();
			} catch (DuplicateIDException e1) {
				addMessage.setText("Duplicate");
				// To convert stack trace to string
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e1.printStackTrace(pw);
				String message = sw.toString() + "\n";
				LogManager.getLogger().severe(message);
			}
		}
		// If cancel is clicked
		else if (e.getSource() == cancel) {
			// To clear all fields
			clear();
		}
		// If the type is selected from the combo box
		else if (e.getSource() == type) {
			// To get the selected vehicle type
			String s = (String) type.getSelectedItem();

			// To check for a match
			switch (s) {
			// If type is Car, set emission to 5
			case "Car":
				autoEmission.setText("5");
				break;
			// If type is Bus, set emission to 10
			case "Bus":
				autoEmission.setText("10");
				break;
			// If type is Truck, set emission to 10
			case "Truck":
				autoEmission.setText("10");
				break;
			// Else, blank
			default:
				autoEmission.setText("");
				break;
			}
		}
	}

	// The vehicle implements Duplicate ID Exception
	// To add new vehicle
	private void addNewVehicle() throws DuplicateIDException {
		// If fields are empty, display message
		if (vuser.getText().length() == 0 || ctuser.getText().length() == 0 || luser.getText().length() == 0) {
			addMessage.setText("Null Field");
		} else {
			try {
				// Clear message
				addMessage.setText("");

				// Remove spaces
				String vNum = vuser.getText().trim().toUpperCase();
				String vType = type.getSelectedItem().toString();
				int vCross = Integer.parseInt(ctuser.getText().trim());
				String vDirection = direction.getSelectedItem().toString();
				int vLength = Integer.parseInt(luser.getText().trim());
				int vEmission = Integer.parseInt(autoEmission.getText());
				String vStatus = status.getText();
				String vSegment = segment.getSelectedItem().toString();

				// If the data is valid
				if (vNum.length() == 6 && Character.isLetter(vNum.charAt(0)) && vNum.substring(1).matches("[0-9]+")
						&& vCross > 0 && vCross <= 15 && vLength > 0 && vLength <= 15) {
					boolean ok = false;

					var v = guiRenderer.generateVehicle(vNum, vType, vCross, vDirection, vLength, vEmission, vStatus,
							vSegment);
					ok = guiRenderer.getiCollection().addOneVehicle(v);
					if (ok == true) {
						addMessage.setText("Success");

						// To clear the fields
						clear();

						// To add new vehicle to queue
						guiRenderer.getiCollection().addToQueue(v);

						// To refresh gui data
						update();
					} else {
						throw new DuplicateIDException("Duplicate Number Plate entered");
					}
				} else {
					addMessage.setText("Wrong data entered");
					String message = "Wrong data entered by the user\n";
					LogManager.getLogger().info(message);
				}
			} catch (NumberFormatException e) {
				addMessage.setText("Wrong data type");
				String message = "Wrong data type entered by the user\n";
				LogManager.getLogger().info(message);
			}
		}
	}

	// To clear all editable fields
	private void clear() {
		vuser.setText("");
		ctuser.setText("");
		luser.setText("");
		// To reset combo boxes to their first/default values
		type.setSelectedIndex(0);
		direction.setSelectedIndex(0);
		segment.setSelectedIndex(0);
	}

	@Override
	// To update the vehicle table, stats table and CO2 value
	public void update() {
		// To update the statistics in the GUI
		guiRenderer.getStm().fireTableDataChanged();

		// To update the vehicles in the GUI
		guiRenderer.getVtm().fireTableDataChanged();

		// To update the co2 in the GUI
		EastPanel.co2.setText(String.format("%.2f", guiRenderer.getiCollection().totalEmissions()));
	}

}
