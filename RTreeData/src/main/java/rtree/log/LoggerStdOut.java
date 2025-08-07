package rtree.log;


public class LoggerStdOut extends LoggerBase {
	
	public LoggerStdOut(LogLevel logLevel) {
		super(logLevel);
	}

	@Override
	public void log(String entry) {
		String lastCategory = entriesCategory.isEmpty() ? null : entriesCategory.get(entriesCategory.size() - 1);
		super.log(entry, lastCategory, LogLevel.DEBUG, true);
		
	}

	@Override
	public void log() {
		String lastCategory = entriesCategory.isEmpty() ? null : entriesCategory.get(entriesCategory.size() - 1);
		super.log("", lastCategory, LogLevel.DEBUG, true);
		
	}
	
}
