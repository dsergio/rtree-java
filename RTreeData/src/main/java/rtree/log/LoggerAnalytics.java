package rtree.log;

public class LoggerAnalytics extends LoggerBase implements ILogger {
	
	private String treeName;
	
	public LoggerAnalytics(LogLevel logLevel, String treeName) {
		super(logLevel);
		this.treeName = treeName;
	}

	@Override
	public void log(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void log() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void logExact(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public LogLevel getLogLevel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLogLevel(LogLevel logLevel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void log(Exception e) {
		// TODO Auto-generated method stub
		
	}

}
