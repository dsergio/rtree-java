package cloudrtree;
import java.util.List;

import org.json.simple.JSONObject;

/**
 * 
 * Description TBD
 * 
 * @author David Sergio
 *
 */
public class Rectangle {
	
	private int x1;
	private int x2;
	private int y1;
	private int y2;
	
	private int level;
	
	public Rectangle(int x1, int x2, int y1, int y2) {
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
	}
	
	public Rectangle() {
		this(0, 0, 0, 0);
	}
	
	public int width() {
		return Math.abs(x2 - x1);
	}
	
	public int height() {
		return Math.abs(y2 - y1);
	}

	public int getX1() {
		return x1;
	}

	public void setX1(int x1) {
		this.x1 = x1;
	}

	public int getX2() {
		return x2;
	}

	public void setX2(int x2) {
		this.x2 = x2;
	}

	public int getY1() {
		return y1;
	}

	public void setY1(int y1) {
		this.y1 = y1;
	}

	public int getY2() {
		return y2;
	}

	public void setY2(int y2) {
		this.y2 = y2;
	}
	
	public int getArea() {
		return width() * height();
	}
	
	public boolean containsPoint(int x, int y) {
		return (x >= x1 && x <= x2 && y >= y1 && y <= y2);
	}
	
	public JSONObject getJson() {
		JSONObject obj = new JSONObject();
		obj.put("x1", x1);
		obj.put("x2", x2);
		obj.put("y1", y1);
		obj.put("y2", y2);
		return obj;
	}
	
	@Override
	public String toString() {
		return "[x1=" + x1 + ", x2=" + x2 + ", y1=" + y1 + ", y2=" + y2 + ", area=" + getArea() + "]";
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	
	static Rectangle sumRectangles(List<Rectangle> rectangles) {
		if (rectangles == null || rectangles.size() == 0) {
			return null;
		}
		if (rectangles.size() > 1) {
			
			int minX = rectangles.get(0).getX1();
			int maxX = rectangles.get(0).getX2();
			int minY = rectangles.get(0).getY1();
			int maxY = rectangles.get(0).getY2();
			for (Rectangle r : rectangles) {
				minX = Math.min(r.getX1(), minX);
				maxX = Math.max(r.getX2(), maxX);
				minY = Math.min(r.getY1(), minY);
				maxY = Math.max(r.getY2(), maxY);
			}
			return new Rectangle(minX, maxX, minY, maxY);
		} else {
			return rectangles.get(0);
		}
	}
	static Rectangle sumRectangles(Rectangle rectangle, LocationItem item) {
		if (rectangle == null) {
			return null;
		}
		if (item == null) {
			return rectangle;
		}
		
		int minX = Math.min(item.getX(), rectangle.getX1());
		int maxX = Math.max(item.getX(), rectangle.getX2());
		int minY = Math.min(item.getY(), rectangle.getY1());
		int maxY = Math.max(item.getY(), rectangle.getY2());
		
		return new Rectangle(minX, maxX, minY, maxY);
		
	}
	static Rectangle sumRectangles(Rectangle r1, Rectangle r2) {
		if (r1 == null && r2 == null) {
			return null;
		} else if (r1 == null) {
			return r2;
		} else if (r2 == null) {
			return r1;
		}
		
		int minX = Math.min(r1.getX1(), r2.getX1());
		int maxX = Math.max(r1.getX2(), r2.getX2());
		int minY = Math.min(r1.getY1(), r2.getY1());
		int maxY = Math.max(r1.getY2(), r2.getY2());
		
		return new Rectangle(minX, maxX, minY, maxY);
		
	}
	
	static boolean rectanglesOverlap(Rectangle r1, Rectangle r2) {
		
		if (r2.getX1() > r1.getX2()) {
			return false;
		}
		if (r2.getY1() > r1.getY2()) {
			return false;
		}
		if (r2.getY2() < r1.getY1()) {
			return false;
		}
		if (r2.getX2() < r1.getX1()) {
			return false;
		}
		return true;
	}
	
	
	static int areaRectangles(Rectangle r1, Rectangle r2) {
		
		int minX = Math.min(r1.getX1(), r2.getX1());
		int maxX = Math.max(r1.getX2(), r2.getX2());
		int minY = Math.min(r1.getY1(), r2.getY1());
		int maxY = Math.max(r1.getY2(), r2.getY2());
		return Math.abs(maxX - minX) * Math.abs(maxY - minY);
	}
}
