package rtree.item;



import java.util.UUID;

import org.json.simple.JSONObject;

public class LocationItemND extends LocationItemBase {
	
	public LocationItemND(int numberDimensions, String id) {
		super(numberDimensions, id);
		if (numberDimensions < 1) {
			throw new IllegalArgumentException("LocationItemND minimum dimension is 1.");
		}
	}

	public LocationItemND(int numberDimensions) {
		super(numberDimensions, UUID.randomUUID().toString());
		if (numberDimensions < 1) {
			throw new IllegalArgumentException("LocationItemND minimum dimension is 1.");
		}
	}
	
	@Override
	public String toString() {
		String str = "(";
		for (int i = 0; i < numberDimensions - 1; i++) {
			str += dimensionArray.get(i) + ", ";
		}
		str += dimensionArray.get(dimensionArray.size() - 1);
		str +=  ") " + type + " " + id;
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
		obj.put("id", id);
		return obj;
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getLocationJson() {
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
		return obj;
	}

}
