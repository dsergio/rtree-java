package rtree.log;

import java.awt.Graphics2D;

public interface ILoggerPaint {

	void log(String message, Graphics2D drawImage, int x, int y);
	
}
