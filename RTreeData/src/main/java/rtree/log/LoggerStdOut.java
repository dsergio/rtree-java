package rtree.log;

public class LoggerStdOut extends LoggerBase {
	
	public LoggerStdOut(LogLevel logLevel) {
		super(logLevel);
	}
	
	@Override
	public void log(String message) {
		
		if (logLevel.equals(LogLevel.DEV) || logLevel.equals(LogLevel.DEV2)) {
			System.out.println(message);
		}
		
	}

	@Override
	public void log() {
		if (logLevel.equals(LogLevel.DEV) || logLevel.equals(LogLevel.DEV2)) {
			System.out.println();
		}
	}

	@Override
	public void logExact(String message) {
		
		if (logLevel.equals(LogLevel.DEV) || logLevel.equals(LogLevel.DEV2)) {
			System.out.print(message);
		}
		
	}
	
}
