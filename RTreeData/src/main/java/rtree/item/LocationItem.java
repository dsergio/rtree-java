package rtree.item;

import org.json.simple.JSONObject;

/**
 * Represents a location item in an R-tree structure with a specified number of dimensions.
 * This class extends {@link LocationItemBase} and provides methods to manage the dimensions
 * and properties of the location item.
 * 
 * @param <T> {@link rtree.item.IRType} representing the type of data stored in each dimension.
 */
public class LocationItem<T extends IRType<T>> extends LocationItemBase<T> {
	
	/**
	 * Constructs a LocationItem with the specified number of dimensions.
	 * 
	 * @param numberDimensions the number of dimensions for this location item
	 * @throws IllegalArgumentException if numberDimensions is less than 1
	 */
	public LocationItem(int numberDimensions) throws IllegalArgumentException {
		super(numberDimensions);
		if (numberDimensions < 1) {
			throw new IllegalArgumentException("LocationItemND minimum dimension is 1.");
		} else {
			
		}
	}
	
	/**
	 * Constructor with the specified number of dimensions and a unique identifier.
	 * 
	 * @param numberDimensions the number of dimensions for this location item
	 * @param id a unique identifier for this location item
	 * @throws IllegalArgumentException if numberDimensions is less than 1
	 */
	public LocationItem(int numberDimensions, String id) throws IllegalArgumentException {
		super(numberDimensions, id);
		if (numberDimensions < 1) {
			throw new IllegalArgumentException("LocationItemND minimum dimension is 1.");
		} else {
			
		}
	}
	
	/**
	 * Returns the location item string representation.
	 * @return a string representation of the location item, including its dimensions and type
	 */
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
		
		JSONObject properties = new JSONObject();
		for (String s : itemProperties.keySet()) {
			properties.put(s, itemProperties.get(s));
		}
		obj.put("properties", properties);
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
