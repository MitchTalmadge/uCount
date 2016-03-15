package org.BinghamTSA.uCount.core.utilities;

import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.jboss.logmanager.Level;

/**
 * Used to log messages to the console and log files on different levels; info, verbose, and error.
 */
public class PollLogger {

  private static final Logger LOGGER = Logger.getLogger("Poll");

  static {
    ConsoleHandler consoleHandler = null;

    // Look for an existing ConsoleHandler so we don't add another.
    for (Handler handler : LOGGER.getHandlers()) {
      if (handler instanceof ConsoleHandler)
        consoleHandler = (ConsoleHandler) handler;
    }

    // No existing ConsoleHandler; add one.
    if (consoleHandler == null) {
      consoleHandler = new ConsoleHandler();
      LOGGER.addHandler(consoleHandler);
    }

    // Set levels for logging.
    LOGGER.setLevel(Level.ALL);
    consoleHandler.setLevel(Level.DEBUG); // Set to DEBUG to see Verbose messages.

    // Set formatter.
    consoleHandler.setFormatter(new PollConsoleFormatter());
  }

  /**
   * For logging messages that will be seen in the console. Used for general messages, not errors.
   * 
   * @param message The message to log.
   */
  public static void logMessage(String message) {
    LOGGER.log(Level.INFO, message);
  }

  /**
   * For logging errors that will be seen in the console.
   * 
   * @param message The error to log.
   */
  public static void logError(String message) {
    LOGGER.log(Level.SEVERE, message);
  }

  /**
   * For logging messages that will only be seen in debug mode.
   * 
   * @param message The verbose message.
   */
  public static void logVerbose(String message) {
    LOGGER.log(Level.DEBUG, message);
  }

  private static class PollConsoleFormatter extends Formatter {

    @Override
    public String format(LogRecord record) {
      return "[" + record.getLevel().getName() + "] " + record.getMessage() + "\n";
    }

  }

}
