package cloudrtree;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public abstract class LocationItemBase {

	protected List<Integer> dimensionArray;
	protected int numberDimensions;
	protected Object data;
	protected String type;
	
	public LocationItemBase(int numberDimensions) {
		this.numberDimensions = numberDimensions;
		dimensionArray = new ArrayList<Integer>();
		while (dimensionArray.size() < numberDimensions) {
			dimensionArray.add(null);
		}
	}
	
	/**
	 * 
	 * @param dim Dimension. 0 for "x", 1 for "y", 2 for "z", etc.
	 * @param value
	 */
	public void setDim(int dim, int value) {
		if (dim < 0 || dim >= numberDimensions) {
			throw new IllegalArgumentException("min dimension 0, max dimension " + numberDimensions);
		}
		dimensionArray.set(dim, value);
	}
	
	/**
	 * 
	 * @param dim Dimension. 0 for "x", 1 for "y", 2 for "z", etc.
	 * @return
	 */
	public Integer getDim(int dim) {
		if (dim < 0 || dim >= numberDimensions) {
			throw new IllegalArgumentException("min dimension 0, max dimension " + numberDimensions);
		}
		return dimensionArray.get(dim);
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public abstract JSONObject getJson();
	
	public static Integer space(LocationItemBase e1, LocationItemBase e2) {
		
		if (e1.dimensionArray.size() != e2.dimensionArray.size()) {
			throw new IllegalArgumentException("Dimensions of items must be the same.");
		}
		
		int spaceTotal = 1; 
		for (int i = 0; i < e1.dimensionArray.size(); i++) {
			if (e1.dimensionArray.get(i) == null || e1.dimensionArray.get(i) == null) {
				throw new IllegalArgumentException("Dimension " + i + " is missing in one of the inputs.");
			}
			spaceTotal = spaceTotal * Math.abs((e1.getDim(i) - e2.getDim(i)));
		}
		
		return spaceTotal;
	}
	
	public static JSONArray getItemsJSON(List<LocationItem> locationItems) {
		
		JSONArray arr = new JSONArray();
		if (locationItems != null) {
			for (LocationItemBase item : locationItems) {
				
				JSONObject obj = new JSONObject();
				
				for (int i = 0; i < item.numberDimensions; i++) {
					switch (i) {
						case 0: 
							obj.put("x", item.getDim(i));
							break;
						case 1:
							obj.put("y", item.getDim(i));
							break;
						case 2:
							obj.put("z", item.getDim(i));
							break;
						default:
							obj.put("" + i, item.getDim(i));
							break;
					}
					obj.put("type", item.getType());

				}
				arr.add(obj);
			}
		}
		return arr;
	}
}
