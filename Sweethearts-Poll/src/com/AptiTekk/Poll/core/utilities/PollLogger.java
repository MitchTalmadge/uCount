package com.AptiTekk.Poll.core.utilities;

import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.jboss.logmanager.Level;

public class PollLogger {

    private static final Logger LOGGER = Logger.getLogger("Poll");
    private static final ConsoleHandler CONSOLE_HANDLER = new ConsoleHandler();

    static {
	LOGGER.setLevel(Level.ALL);
	CONSOLE_HANDLER.setLevel(Level.DEBUG);
	
	CONSOLE_HANDLER.setFormatter(new PollConsoleFormatter());
	LOGGER.addHandler(CONSOLE_HANDLER);
    }

    /**
     * For logging messages that will be seen in the console. Used for general
     * messages, not errors.
     * 
     * @param message
     *            The message to log.
     */
    public static void logMessage(String message) {
	LOGGER.log(Level.INFO, message);
    }

    /**
     * For logging errors that will be seen in the console.
     * 
     * @param message
     *            The error to log.
     */
    public static void logError(String message) {
	LOGGER.log(Level.SEVERE, message);
    }

    /**
     * For logging messages that will only be seen in debug mode.
     * 
     * @param message
     *            The verbose message.
     */
    public static void logVerbose(String message) {
	LOGGER.log(Level.DEBUG, message);
    }

    private static class PollConsoleFormatter extends Formatter {

	@Override
	public String format(LogRecord record) {
	    return "["+record.getLevel().getName()+"] "+record.getMessage()+"\n";
	}
	
    }
    
}
