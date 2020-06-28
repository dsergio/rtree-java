package rtree.log;

public interface ILogger {
	
	public void log(String message); // include a newline
	public void log(); // log a newline
	public void logExact(String message); // no newline
	public LogLevel getLogLevel();
	public void setLogLevel(LogLevel logLevel);
	public void log(Exception e);
}
