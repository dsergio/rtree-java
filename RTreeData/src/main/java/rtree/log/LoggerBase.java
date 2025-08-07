package rtree.log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class LoggerBase implements ILogger {

	protected LogLevel logLevel;
	
	protected List<String> entries;
	protected List<String> entriesCategory;
	protected List<LogLevel> entriesLogLevel;
	protected int currentEntryIndex = 0;
	
	protected File logFile;
	protected FileWriter fileWriter;
	
	
	
	public LoggerBase(LogLevel logLevel, String logFilePath) {
		this.logLevel = logLevel;
		this.entries = new ArrayList<>();
		this.entriesCategory = new ArrayList<>();
		this.entriesLogLevel = new ArrayList<>();
		
		if (logFilePath != null && !logFilePath.isEmpty()) {
			
			this.logFile = new File(logFilePath);
			try {
				this.fileWriter = new FileWriter(logFile, true);
			} catch (IOException e) {
				System.err.println("Error initializing file writer for log file: " + logFilePath);
				logFile = null;
				fileWriter = null;
				log(e);
			}
			
		}
	}
	
	public LoggerBase(LogLevel logLevel) {
		this(logLevel, null);
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
		String logEntry = e.getClass().getName() + ": " + e.getMessage();
		System.out.print(logEntry);
		entries.add(logEntry);
		entriesCategory.add("EXCEPTION");
		entriesLogLevel.add(LogLevel.ERROR);
	}
	
	@Override
	public void log(String entry, String category, LogLevel logLevel, boolean newLine) {
		if (this.logLevel.compareTo(logLevel) >= 0) {
			
			if (entry == null) {
				entry = "";
			}
			if (newLine) {
				entry += "\n";
			}
			
			if (category == null || category.isEmpty()) {
				category = "UNKNOWN";
			}
			
			entries.add(entry);
			entriesCategory.add(category.toUpperCase());
			entriesLogLevel.add(logLevel);
		}
		
		printLogEntries();
	}
	
	@Override
	public void printLogEntries() {
		printLogEntries(null, null);
	}
	
	@Override
	public void printLogEntries(String filterCategory, LogLevel filterLogLevel) {
		
//		System.out.println("current entry index: " + currentEntryIndex);
		
		for (int i = currentEntryIndex; i < entries.size(); i++) {
			
//			System.out.println("Log Level: " + entriesLogLevel.get(i).compareTo(logLevel));
			
			String output = null;
			String outputCategory = null;
			
			if (entriesCategory.get(i).equals("CSV")) {
				outputCategory = "";
			} else {
				outputCategory = "[" + entriesCategory.get(i) + "] ";
			}
			
			if (filterCategory == null && filterLogLevel == null) {
				if (entriesLogLevel.get(i).compareTo(logLevel) >= 0) {
					output = outputCategory + entries.get(i);
				}
			} else if (filterCategory != null && filterLogLevel == null) {
				if (entriesCategory.get(i).equals(filterCategory) && entriesLogLevel.get(i).compareTo(logLevel) >= 0) {
					output = outputCategory + entries.get(i);
				}
			} else if (filterCategory == null && filterLogLevel != null) {
				if (entriesLogLevel.get(i).equals(filterLogLevel) && entriesLogLevel.get(i).compareTo(logLevel) >= 0) {
					output = outputCategory + entries.get(i);
				}
			} else if (filterCategory != null && filterLogLevel != null) {
				if (entriesCategory.get(i).equals(filterCategory) && entriesLogLevel.get(i).equals(filterLogLevel)
						&& entriesLogLevel.get(i).compareTo(logLevel) >= 0) {
					output = outputCategory + entries.get(i);
				}
			}
			
			if (output != null) {
				
				System.out.print(output);
				
				if (fileWriter != null) {
					try {
						fileWriter.write(output);
						fileWriter.flush();
					} catch (IOException e) {
						System.err.println("Error writing to log file: " + logFile.getAbsolutePath());
						log(e);
					}
					
				}
				
			}
			
			currentEntryIndex++;
		}
		
		
	}
	
	

}
