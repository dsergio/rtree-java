package rtree.log;

public interface ILogger {
	
	 
	 /**
	 * Logs an entry with a category and log level and a newline if specified.
	 * @param entry
	 * @param category
	 * @param logLevel
	 * @param newLine
	 */
	 void log(String entry, String category, LogLevel logLevel, boolean newLine);
	 
	 /**
	  * Logs an entry with the last category and log level and a newline
	  * @param entry
	  */
	 void log(String entry);
	 
	 /**
	  * Logs an entry with only a newline, using the last category and logLevel.
	  */
	 void log();
	 
	 /**
	  * Gets the current log level.
	  * @return
	  */
	 LogLevel getLogLevel();
	 
	 /**
	  * Sets the log level.
	  * @param logLevel
	  */
	 void setLogLevel(LogLevel logLevel);
	 
	 /**
	  * Logs an exception with its class name and message.
	  * @param e
	  */
	 void log(Exception e);
	 
	 
	 
	 /**
	  * Prints all log entries that match the specified category and log level.
	  * @param filterCategory
	  * @param filterLogLevel
	  */
	 void printLogEntries(String filterCategory, LogLevel filterLogLevel);
	 
	 /**
	  * Prints all log entries.
	  */
	 void printLogEntries(); 
}
