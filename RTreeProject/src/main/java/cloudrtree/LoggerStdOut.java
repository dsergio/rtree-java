package cloudrtree;

public class LoggerStdOut implements ILogger {

	private LogLevel logLevel;
	
	public LoggerStdOut(LogLevel logLevel) {
		this.logLevel = logLevel;
	}
	
	@Override
	public void log(String message) {
		
		if (logLevel.equals(LogLevel.DEV) || logLevel.equals(LogLevel.DEV2)) {
			System.out.println(message);
		}
		
	}

	@Override
	public void log() {
		System.out.println();
	}

	@Override
	public void logExact(String message) {
		
		if (logLevel.equals(LogLevel.DEV) || logLevel.equals(LogLevel.DEV2)) {
			System.out.print(message);
		}
		
	}

	@Override
	public LogLevel getLogLevel() {
		return logLevel;
	}

	@Override
	public void setLogLevel(LogLevel logLevel) {
		this.logLevel = logLevel;
	}

}
