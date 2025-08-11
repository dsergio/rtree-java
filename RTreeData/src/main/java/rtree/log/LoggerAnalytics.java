package rtree.log;

import java.io.IOException;

/**
 * LoggerAnalytics class for logging analytics data.
 * This class extends LoggerBase and implements ILogger.
 */
public class LoggerAnalytics extends LoggerBase implements ILogger {
	
	// I'm not sure why I initially included this
//	private String treeName;
	
	/**
	 * Constructor for LoggerAnalytics.
	 * @param logLevel the log level to set for the logger
	 * @throws IOException if an I/O error occurs during logger initialization
	 */
	public LoggerAnalytics(LogLevel logLevel) throws IOException {
		super(logLevel);
//		this.treeName = treeName;
	}

	@Override
	public void log(String entry) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void log() {
		// TODO Auto-generated method stub
		
	}

	

}
