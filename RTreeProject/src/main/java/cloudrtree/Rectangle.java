package cloudrtree;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * 
 * 2-dimensional Rectangle class
 * 
 * @author David Sergio
 *
 */
public class Rectangle extends HyperRectangleBase {
	
	
	public Rectangle(int x1, int x2, int y1, int y2) {
		super(2);
		super.setDim1(0, x1);
		super.setDim2(0, x2);
		super.setDim1(1, y1);
		super.setDim2(1, y2);
	}
	
	public Rectangle(String retangleStr) {
		super(2);
		JSONParser parser;
		Object obj;
		
		parser = new JSONParser();
		try {
			if (retangleStr != null) {
				obj = parser.parse(retangleStr);
				JSONObject rectObj = (JSONObject) obj;
				super.setDim1(0, Integer.parseInt(rectObj.get("x1").toString()));
				super.setDim2(0, Integer.parseInt(rectObj.get("x2").toString()));
				super.setDim1(1, Integer.parseInt(rectObj.get("y1").toString()));
				super.setDim2(1, Integer.parseInt(rectObj.get("y2").toString()));
			}
			
			
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public Rectangle() {
		this(0, 0, 0, 0);
	}
	
	public int getArea() {
		return Math.abs(getDim1(0) - getDim1(0)) * Math.abs(getDim1(1) - getDim1(1));
	}
	
	public boolean containsPoint(int x, int y) {
		return (x >= getDim1(0) && x <= getDim2(0) && y >= getDim1(1) && y <= getDim2(1));
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject getJson() {
		JSONObject obj = new JSONObject();
		obj.put("x1", getDim1(0));
		obj.put("x2", getDim2(0));
		obj.put("y1", getDim1(1));
		obj.put("y2", getDim2(1));
		return obj;
	}
	
	@Override
	public String toString() {
		return "[x1=" + getDim1(0) + ", x2=" + getDim2(0) + ", y1=" + getDim1(1) + ", y2=" + getDim2(1) + ", area=" + getArea() + "]";
	}
	
	static IHyperRectangle sumRectangles(List<IHyperRectangle> rectangles) {
		if (rectangles == null || rectangles.size() == 0) {
			return null;
		}
		if (rectangles.size() > 1) {
			
			int minX = rectangles.get(0).getDim1(0);
			int maxX = rectangles.get(0).getDim2(0);
			int minY = rectangles.get(0).getDim1(1);
			int maxY = rectangles.get(0).getDim2(1);
			for (IHyperRectangle r : rectangles) {
				minX = Math.min(r.getDim1(0), minX);
				maxX = Math.max(r.getDim2(0), maxX);
				minY = Math.min(r.getDim1(1), minY);
				maxY = Math.max(r.getDim2(1), maxY);
			}
			return new Rectangle(minX, maxX, minY, maxY);
		} else {
			return rectangles.get(0);
		}
	}
	static IHyperRectangle sumRectangles(IHyperRectangle rectangle, ILocationItem item) {
		if (rectangle == null) {
			return null;
		}
		if (item == null) {
			return rectangle;
		}
		
		int minX = Math.min(item.getDim(0), rectangle.getDim1(0));
		int maxX = Math.max(item.getDim(0), rectangle.getDim2(0));
		int minY = Math.min(item.getDim(1), rectangle.getDim1(1));
		int maxY = Math.max(item.getDim(1), rectangle.getDim2(1));
		
		return new Rectangle(minX, maxX, minY, maxY);
		
	}
	static Rectangle twoPointsRectangles(ILocationItem item1, ILocationItem item2) {
		if (item1 == null || item2 == null) {
			return null;
		}
		
		
		int minX = Math.min(item1.getDim(0), item2.getDim(0));
		int maxX = Math.max(item1.getDim(0), item2.getDim(0));
		int minY = Math.min(item1.getDim(1), item2.getDim(1));
		int maxY = Math.max(item1.getDim(1), item2.getDim(1));
		
		return new Rectangle(minX, maxX, minY, maxY);
		
	}
	
	static IHyperRectangle sumRectangles(IHyperRectangle r1, IHyperRectangle r2) {
		if (r1 == null && r2 == null) {
			return null;
		} else if (r1 == null) {
			return r2;
		} else if (r2 == null) {
			return r1;
		}
		
		int minX = Math.min(r1.getDim1(0), r2.getDim1(0));
		int maxX = Math.max(r1.getDim2(0), r2.getDim2(0));
		int minY = Math.min(r1.getDim1(1), r2.getDim1(1));
		int maxY = Math.max(r1.getDim2(1), r2.getDim2(1));
		
		return new Rectangle(minX, maxX, minY, maxY);
		
	}
	
	static boolean rectanglesOverlap(IHyperRectangle r1, IHyperRectangle r2) {
		
		if (r2.getDim1(0) > r1.getDim2(0)) {
			return false;
		}
		if (r2.getDim1(1) > r1.getDim2(1)) {
			return false;
		}
		if (r2.getDim2(1) < r1.getDim1(1)) {
			return false;
		}
		if (r2.getDim2(0) < r1.getDim1(0)) {
			return false;
		}
		return true;
	}
	
	
	static int areaRectangles(IHyperRectangle r1, IHyperRectangle r2) {
		
		int minX = Math.min(r1.getDim1(0), r2.getDim1(0));
		int maxX = Math.max(r1.getDim2(0), r2.getDim2(0));
		int minY = Math.min(r1.getDim1(1), r2.getDim1(1));
		int maxY = Math.max(r1.getDim2(1), r2.getDim2(1));
		return Math.abs(maxX - minX) * Math.abs(maxY - minY);
	}
}
