package f21as.group2.controller;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

// Used for limiting characters within the JField
public class TextLimiter extends PlainDocument {
	private static final long serialVersionUID = 1L;
	private int limit;

	// Constructor with the number of characters passed
	public TextLimiter(int limit) {
		super();
		this.limit = limit;
	}

	@Override
	public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
		if (str == null)
			return;

		if ((getLength() + str.length()) <= limit) {
			super.insertString(offset, str, attr);
		}
	}
}
