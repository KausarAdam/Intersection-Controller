package f21as.group2.controller;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

// Singleton
// To record details in a log
public class LogManager {

	private static Logger logger;

	// Private to ensure existance of only one instance
	private LogManager() {
		logger = null;
	}

	public static Logger getLogger() {
		if (logger == null) {
			try {
				// To write the log to a file and append it with the previous file
				FileHandler handler = new FileHandler("Simulator Log.log", true);

				// To create or find a logger
				logger = Logger.getLogger("Simulator Log");
				// To receive logging messages and write them to the file
				logger.addHandler(handler);

				// To add detailed tracing messages
				logger.finest("Session started - " + new java.util.Date());
			} catch (IOException e) {
				String message = "A technical problem has occurred while creating the log file. \nThe program must stop.";
				LogManager.getLogger().severe(message);
				System.exit(1);
			}
		}
		return logger;
	}

}
