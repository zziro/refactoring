package com.company.logger.job;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JobLogger {

	/**
	 * Create enums to  predefine, log level and the place to store the log (File, Console. Database)
	 * */
	private static boolean logToFile; 
	private static boolean logToConsole;
	private static boolean logMessage;
	private static boolean logWarning;
	private static boolean logError;
	private static boolean logToDatabase;
	private boolean initialized; // remove this property due that this is not used in the application
	private static Map dbParams;
	private static Logger logger;

	public JobLogger(boolean logToFileParam, boolean logToConsoleParam, boolean logToDatabaseParam,
			boolean logMessageParam, boolean logWarningParam, boolean logErrorParam, Map dbParamsMap) {
		logger = Logger.getLogger("MyLog");
		logError = logErrorParam;
		logMessage = logMessageParam;
		logWarning = logWarningParam;
		logToDatabase = logToDatabaseParam;
		logToFile = logToFileParam;
		logToConsole = logToConsoleParam;
		dbParams = dbParamsMap;
	}

	/**
	* Methods names should be verbs and the first letter should be on lowercase and other should have capitalize letter
	* */
	public static void LogMessage(String messageText, boolean message, boolean warning, boolean error)
			throws Exception {
		messageText.trim();
		if (messageText == null || messageText.length() == 0) {
			return; //Throw an IllegalArgumentException and request the user to enter a value for message variable, so the user should know what do; making the application more usable.
		}
		if (!logToConsole && !logToFile && !logToDatabase) { // Create a enum to classify the places/destination to store all logs such as CONSOLE, FILE or DATABASE
			throw new Exception("Invalid configuration"); // Use constants to put predefine messages.
		}
		if ((!logError && !logMessage && !logWarning) || (!message && !warning && !error)) { // Create a enum to classify the level of logs namely ERROR; MESSAGE or WARNING
			throw new Exception("Error or Warning or Message must be specified");
		}

		Connection connection = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", dbParams.get("userName"));
		connectionProps.put("password", dbParams.get("password"));

		connection = DriverManager.getConnection("jdbc:" + dbParams.get("dbms") + "://" + dbParams.get("serverName") // Use StringBuilder because is much faster and it consumes less memory. And also create to method to manage connections to database.
				+ ":" + dbParams.get("portNumber") + "/", connectionProps);

		int t = 0;
		if (message && logMessage) {
			t = 1; // Use enum to classify the log level
		}

		if (error && logError) {
			t = 2;
		}

		if (warning && logWarning) {
			t = 3;
		}

		Statement stmt = connection.createStatement();

		String l = null;
		File logFile = new File(dbParams.get("logFileFolder") + "/logFile.txt"); // Create a method for, print in a file functionality. It will prevent the have spaghetti code.
			if (!logFile.exists()) {
			logFile.createNewFile();
		}

		FileHandler fh = new FileHandler(dbParams.get("logFileFolder") + "/logFile.txt");
		ConsoleHandler ch = new ConsoleHandler(); // Create a method to put the, print to the console functionality, to prevent spaghetti code.

		if (error && logError) {
			l = l + "error " + DateFormat.getDateInstance(DateFormat.LONG).format(new Date()) + messageText; // Classify the erroe message using enums
		}

		if (warning && logWarning) {
			l = l + "warning " + DateFormat.getDateInstance(DateFormat.LONG).format(new Date()) + messageText;
		}

		if (message && logMessage) {
			l = l + "message " + DateFormat.getDateInstance(DateFormat.LONG).format(new Date()) + messageText;
		}

		if (logToFile) {
			logger.addHandler(fh);
			logger.log(Level.INFO, messageText);
		}

		if (logToConsole) {
			logger.addHandler(ch);
			logger.log(Level.INFO, messageText);
		}

		if (logToDatabase) {
			stmt.executeUpdate("insert into Log_Values('" + message + "', " + String.valueOf(t) + ")");
		}
	}
}
