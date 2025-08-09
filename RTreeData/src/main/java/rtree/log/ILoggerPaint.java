package rtree.log;

import java.awt.Graphics2D;

/**
 * Interface for logging messages to a graphics context.
 * This can be used to draw log entries on a graphical interface.
 * 
 * @author David Sergio
 */
public interface ILoggerPaint {
	
	/**
	 * Logs a message to the graphics context at specified coordinates.
	 * @param category log category
	 * @param entry log entry message
	 * @param drawImage graphics context to draw the log entry
	 * @param x x-coordinate for drawing the log entry
	 * @param y y-coordinate for drawing the log entry
	 */
	void log(String category, String entry, Graphics2D drawImage, int x, int y);
	
}
