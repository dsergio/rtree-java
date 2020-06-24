package cloudrtree;

import java.awt.Graphics2D;

public class LoggerPaint extends LoggerBase implements ILoggerPaint {	
	
	public LoggerPaint(LogLevel logLevel) {
		super(logLevel);
	}
	
	public void log(String message, Graphics2D drawImage, int x, int y) {
		if (logLevel.equals(LogLevel.DEV) || logLevel.equals(LogLevel.DEV2)) {
			drawImage.drawString(message, x, y);
			log(message + ", x = " + x + ", y = " + y);
		}
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
