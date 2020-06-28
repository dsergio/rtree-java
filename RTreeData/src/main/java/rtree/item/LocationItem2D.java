package rtree.item;
import org.json.simple.JSONObject;

/**
 * 
 * LocationItem Implementation
 * 
 * @author David Sergio
 *
 */
public class LocationItem2D extends LocationItemBase{
	
	private int x;
	private int y;
	
	public LocationItem2D(int x, int y, String type) {
		super(2);
		super.setDim(0, x);
		super.setDim(1, y);
		
		this.x = x;
		this.y = y;
		this.type = type;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
		super.setDim(0, x);
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
		super.setDim(1, y);
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getJson() {
		JSONObject obj = new JSONObject();
		obj.put("x", x);
		obj.put("y", y);
		obj.put("type", type);
		return obj;
	}
	
	@Override
	public String toString() {
		String str = "(" + x + ", " + y + ") " + type;
		return str;
	}
	
	static int area2D(LocationItem2D e1, LocationItem2D e2) {
		return Math.abs(e1.getX() - e2.getX()) * Math.abs(e1.getY() - e2.getY());
	}
}
