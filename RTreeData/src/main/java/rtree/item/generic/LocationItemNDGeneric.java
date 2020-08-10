package rtree.item.generic;

import org.json.simple.JSONObject;

public class LocationItemNDGeneric<T extends IRType<T>> extends LocationItemBaseGeneric<T> {

	public LocationItemNDGeneric(int numberDimensions) {
		super(numberDimensions);
		if (numberDimensions < 1) {
			throw new IllegalArgumentException("LocationItemND minimum dimension is 1.");
		} else {
			
		}
	}
	public LocationItemNDGeneric(int numberDimensions, String id) {
		super(numberDimensions, id);
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
				obj.put("x", getDim(i).getData().toString());
				break;
			case 1:
				obj.put("y", getDim(i).getData().toString());
				break;
			case 2:
				obj.put("z", getDim(i).getData().toString());
				break;
			default:
				obj.put("" + i, getDim(i).getData().toString());
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
				obj.put("x", getDim(i).getData().toString());
				break;
			case 1:
				obj.put("y", getDim(i).getData().toString());
				break;
			case 2:
				obj.put("z", getDim(i).getData().toString());
				break;
			default:
				obj.put("" + i, getDim(i).getData().toString());
				break;
			}
		}
		return obj;
	}

}
