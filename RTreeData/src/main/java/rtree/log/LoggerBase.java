package rtree.log;

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
	
	@Override
	public void log(Exception e) {
		System.out.print(e.getClass().getName() + ": " + e.getMessage());
	}

}
