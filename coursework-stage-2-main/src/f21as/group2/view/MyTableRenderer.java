package f21as.group2.view;

import f21as.group2.controller.*;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

//Custom DefaultTableCellRenderer to change the colour of the rows in the phase table according to the activated phase
public class MyTableRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;
	// To store the current phase
	private int phase;
	private int remainder;

	// Constructor
	public MyTableRenderer(int phase, int remainder) {
		this.phase = phase;
		this.remainder = remainder;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		// To set green for the activated phase and red for the rest
		if (row == phase) {
			if (remainder <= CentralController.getOrangeDuration()) {
				c.setBackground(Color.ORANGE);
				// To set the font colour to black
				c.setForeground(Color.BLACK);
			} else {
				// RGBA for green
				Color myColor = new Color(0, 255, 0, 125);
				c.setBackground(myColor);
				// To set the font colour to black
				c.setForeground(Color.BLACK);
			}
		} else {
			// RGBA for red
			Color myColor = new Color(255, 0, 0, 200);
			c.setBackground(myColor);
			// To set the font colour to white
			c.setForeground(Color.WHITE);
		}
		return c;
	}
}
