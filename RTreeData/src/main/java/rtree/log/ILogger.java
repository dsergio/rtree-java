package rtree.log;

/**
 * Interface for logging system.
 * Provides methods to log messages with different categories and log levels,
 * manage the current log level, and print log entries.
 * 
 */
public interface ILogger {
	 
	 /**
	 * Logs an entry with a category and log level and a newline if specified.
	 * @param entry the log entry message
	 * @param category the category of the log entry
	 * @param logLevel the log level of the entry
	 * @param newLine whether to add a newline after the entry
	 */
	 void log(String entry, String category, LogLevel logLevel, boolean newLine);
	 
	 /**
	  * Logs an entry with the last category and log level and a newline
	  * @param entry the log entry message
	  */
	 void log(String entry);
	 
	 /**
	  * Logs an entry with only a newline, using the last category and logLevel.
	  */
	 void log();
	 
	 /**
	  * Gets the current log level.
	  * @return the current log level
	  */
	 LogLevel getLogLevel();
	 
	 /**
	  * Sets the log level.
	  * @param logLevel the log level to set
	  */
	 void setLogLevel(LogLevel logLevel);
	 
	 /**
	  * Logs an exception with its class name and message.
	  * @param e the exception to log
	  */
	 void log(Exception e);
	 
	 /**
	  * Prints all log entries that match the specified category and log level.
	  * @param filterCategory filter by category, or null to ignore category
	  * @param filterLogLevel filter by log level, or null to ignore log level
	  */
	 void printLogEntries(String filterCategory, LogLevel filterLogLevel);
	 
	 /**
	  * Prints all log entries.
	  */
	 void printLogEntries(); 
}
