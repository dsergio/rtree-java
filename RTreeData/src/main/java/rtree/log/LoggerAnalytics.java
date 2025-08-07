package rtree.log;

import java.io.IOException;

public class LoggerAnalytics extends LoggerBase implements ILogger {
	
	private String treeName;
	
	public LoggerAnalytics(LogLevel logLevel, String treeName) throws IOException {
		super(logLevel);
		this.treeName = treeName;
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
