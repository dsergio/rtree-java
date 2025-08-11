package rtree.log;


/**
 * LoggerStdOut is a logger that outputs log entries to the standard output (console).
 */
public class LoggerStdOut extends LoggerBase {
	
	/**
	 * Constructor for LoggerStdOut
	 * @param logLevel The log level for this logger.
	 */
	public LoggerStdOut(LogLevel logLevel) {
		super(logLevel);
	}

	@Override
	public void log(String entry) {
		String lastCategory = entriesCategory.isEmpty() ? null : entriesCategory.get(entriesCategory.size() - 1);
		super.log(entry, lastCategory, LogLevel.DEBUG, true);
		
	}

	@Override
	public void log() {
		String lastCategory = entriesCategory.isEmpty() ? null : entriesCategory.get(entriesCategory.size() - 1);
		super.log("", lastCategory, LogLevel.DEBUG, true);
		
	}
	
}
