package rtree.log;

public interface ILogger {
	
	 void log(String message); // include a newline
	 void log(); // log a newline
	 void logExact(String message); // no newline
	 LogLevel getLogLevel();
	 void setLogLevel(LogLevel logLevel);
	 void log(Exception e);
}
