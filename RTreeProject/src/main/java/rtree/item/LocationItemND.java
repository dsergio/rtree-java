package rtree.item;

import org.json.simple.JSONObject;

public class LocationItemND extends LocationItemBase {

	public LocationItemND(int numberDimensions) {
		super(numberDimensions);
		if (numberDimensions < 1) {
			throw new IllegalArgumentException("LocationItemND minimum dimension is 1.");
		} else {
			
		}
	}
	
	@Override
	public String toString() {
		String str = "(";
		for (int i = 0; i < numberDimensions - 1; i++) {
			str += dimensionArray.get(i) + ", ";
		}
		str += dimensionArray.get(dimensionArray.size() - 1);
		str +=  ") " + type;
		return str;
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getJson() {
		JSONObject obj = new JSONObject();
		for (int i = 0; i < numberDimensions; i++) {
			
			switch (i) {
			case 0: 
				obj.put("x", getDim(i));
				break;
			case 1:
				obj.put("y", getDim(i));
				break;
			case 2:
				obj.put("z", getDim(i));
				break;
			default:
				obj.put("" + i, getDim(i));
				break;
			}
		}
		obj.put("type", type);
		return obj;
	}

}
