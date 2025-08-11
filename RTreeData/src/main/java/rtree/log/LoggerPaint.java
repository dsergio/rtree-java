package rtree.log;

import java.awt.Graphics2D;

/**
 * LoggerPaint is a specialized logger that logs messages to the console
 * and draws them on a provided Graphics2D context.
 * It extends LoggerBase and implements ILoggerPaint.
 */
public class LoggerPaint extends LoggerBase implements ILoggerPaint {	
	
	/**
	 * Constructor for LoggerPaint.
	 * @param logLevel the logging level to set for this logger
	 */
	public LoggerPaint(LogLevel logLevel) {
		super(logLevel);
	}
	
	/**
	 * Logs a message without a category to the console and draws it on the provided
	 * Graphics2D context.
	 * 
	 * @param message   the message to log
	 * @param drawImage the graphics context to draw the message on
	 * @param x x-coordinate for drawing the message
	 * @param y y-coordinate for drawing the message
	 */
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
