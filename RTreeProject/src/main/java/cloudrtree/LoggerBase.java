package cloudrtree;

public abstract class LoggerBase implements ILogger {

	protected LogLevel logLevel;
	
	public LoggerBase(LogLevel logLevel) {
		this.logLevel = logLevel;
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
