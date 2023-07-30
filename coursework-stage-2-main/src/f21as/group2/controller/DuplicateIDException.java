package f21as.group2.controller;

// Custom-made exception to check for duplicate vehicle plate numbers
public class DuplicateIDException extends Exception {

	private static final long serialVersionUID = 1L;

	public DuplicateIDException(String message) {
		super(message);
	}

}
