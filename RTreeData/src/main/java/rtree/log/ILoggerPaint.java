package rtree.log;

import java.awt.Graphics2D;

public interface ILoggerPaint {
	
	/**
	 * Logs a message to the graphics context at specified coordinates.
	 * @param category
	 * @param entry
	 * @param drawImage
	 * @param x
	 * @param y
	 */
	void log(String category, String entry, Graphics2D drawImage, int x, int y);
	
}
