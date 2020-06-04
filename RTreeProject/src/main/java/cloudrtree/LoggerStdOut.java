package cloudrtree;

public class StdOutLogger implements ILogger {

	private LogLevel logLevel;
	
	public StdOutLogger(LogLevel logLevel) {
		this.logLevel = logLevel;
	}
	
	@Override
	public void log(String message) {
		
		if (logLevel.equals(LogLevel.DEV)) {
			System.out.println(message);
		}
		
	}

	@Override
	public void log() {
		System.out.println();
	}

	@Override
	public void logExact(String message) {
		
		if (logLevel.equals(LogLevel.DEV)) {
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
