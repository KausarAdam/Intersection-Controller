package f21as.group2.main;

import f21as.group2.controller.*;

// Calls the manager
public class Main {
	static Manager manage;

	public static void main(String[] args) throws DuplicateIDException {
		System.out.println("This project is submitted by Group 2.\n");
		manage = new Manager();
		manage.run();
	}
}