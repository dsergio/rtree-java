package rtree.log;

import java.awt.Graphics2D;

public class LoggerPaint extends LoggerBase implements ILoggerPaint {	
	
	public LoggerPaint(LogLevel logLevel) {
		super(logLevel);
	}
	
	public void log(String message, Graphics2D drawImage, int x, int y) {
		
		if (logLevel.equals(LogLevel.DEBUG)) {
			
			drawImage.drawString(message, x, y);
			
			String logEntry = message + ", x = " + x + ", y = " + y;
			log(logEntry, "PAINT", LogLevel.DEBUG, true);
		}
		
	}

	@Override
	public void log(String message) {
		
		if (logLevel.equals(LogLevel.DEBUG)) {
			System.out.println(message);
		}
		
	}

	@Override
	public void log() {
		super.log("", null, LogLevel.DEBUG, true);
		
	}

	@Override
	public void log(String category, String entry, Graphics2D drawImage, int x, int y) {
		super.log(entry, category, LogLevel.DEBUG, true);
		
	}

}
