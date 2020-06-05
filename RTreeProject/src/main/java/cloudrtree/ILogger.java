package cloudrtree;

public interface ILogger {
	
	public enum LogLevel {
		DEV,
		DEV2,
		PROD
	}
	public void log(String message); // include a newline
	public void log(); // log a newline
	public void logExact(String message); // no newline
	public LogLevel getLogLevel();
	public void setLogLevel(LogLevel logLevel);
}
