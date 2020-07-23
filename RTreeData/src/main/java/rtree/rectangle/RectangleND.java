package rtree.rectangle;

import org.json.simple.JSONObject;

/**
 * 
 * @author David Sergio
 *
 */
public class RectangleND extends HyperRectangleBase {

	public RectangleND(int numberDimensions) {
		super(numberDimensions);
		if (numberDimensions < 1) {
			throw new IllegalArgumentException("HyperCuboid minimum dimension is 1.");
		} else {
			
		}
	}

	@SuppressWarnings("unchecked")
	public JSONObject getJson() {
		
		JSONObject obj = new JSONObject();
		for (int i = 0; i < numberDimensions; i++) {
			
			switch (i) {
			case 0: 
				obj.put("x1", getDim1(i));
				obj.put("x2", getDim2(i));
				break;
			case 1:
				obj.put("y1", getDim1(i));
				obj.put("y2", getDim2(i));
				break;
			case 2:
				obj.put("z1", getDim1(i));
				obj.put("z2", getDim2(i));
				break;
			default:
				obj.put(i + "_1", getDim1(i));
				obj.put(i + "_2", getDim2(i));
				break;
			}
		}
		obj.put("level", level);
		return obj;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String toString() {
		
		JSONObject obj = getJson();
		obj.put("area", getSpace());
		
		return obj.toJSONString();
		
	}

}
