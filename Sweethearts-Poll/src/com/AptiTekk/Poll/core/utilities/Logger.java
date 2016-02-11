package com.AptiTekk.Poll.core.utilities;

public class Logger {

	// TODO: Proper logging

	private static final boolean VERBOSE_ENABLED = false;

	/**
	 * For logging messages that will be seen in the console no matter what.
	 * Used for general messages, not errors.
	 * 
	 * @param message
	 *            The message to log.
	 */
	public static void logMessage(String message) {
		System.out.println("[INFO]: " + message);
	}

	/**
	 * For logging errors that will be seen in the console.
	 * 
	 * @param message
	 *            The error to log.
	 */
	public static void logError(String message) {
		System.out.println("[ERROR]: " + message);
	}

	/**
	 * For logging messages that will only be seen in debug mode.
	 * 
	 * @param message
	 *            The verbose message.
	 */
	public static void logVerbose(String message) {
		if (VERBOSE_ENABLED)
			System.out.println("[VERBOSE]: " + message);
	}

}
