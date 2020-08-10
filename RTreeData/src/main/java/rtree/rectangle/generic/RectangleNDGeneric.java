package rtree.rectangle.generic;

import org.json.simple.JSONObject;

import rtree.item.generic.IRType;

/**
 * 
 * @author David Sergio
 *
 */
public class RectangleNDGeneric<T extends IRType<T>> extends HyperRectangleBaseGeneric<T> {

	public RectangleNDGeneric(int numberDimensions) {
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
				obj.put("x1", getDim1(i).getData().toString());
				obj.put("x2", getDim2(i).getData().toString());
				break;
			case 1:
				obj.put("y1", getDim1(i).getData().toString());
				obj.put("y2", getDim2(i).getData().toString());
				break;
			case 2:
				obj.put("z1", getDim1(i).getData().toString());
				obj.put("z2", getDim2(i).getData().toString());
				break;
			default:
				obj.put(i + "_1", getDim1(i).getData().toString());
				obj.put(i + "_2", getDim2(i).getData().toString());
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
