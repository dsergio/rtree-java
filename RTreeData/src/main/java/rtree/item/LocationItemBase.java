package rtree.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Abstract base class for location items in an R-tree structure.
 * @param <T> {@link rtree.item.IRType}
 */
public abstract class LocationItemBase<T extends IRType<T>> implements ILocationItem<T> {

	/**
	 * list of dimensions for the location item.
	 */
	protected List<T> dimensionArray;
	
	/**
	 * The number of dimensions for the location item.
	 */
	protected int numberDimensions;
	
	/**
	 * Properties of the location item, stored as key-value pairs.
	 */
	protected Map<String, String> itemProperties;
	
	/**
	 * Type of the location item, used for categorization.
	 */
	protected String type;
	
	/**
	 * Unique identifier for the location item.
	 */
	protected final String id;
	
	/**
	 * Constructor to create a location item with a specified number of dimensions
	 * and a unique ID.
	 * 
	 * @param numberDimensions The number of dimensions for the location item.
	 * @param id               The unique identifier for the location item.
	 */
	public LocationItemBase(int numberDimensions, String id) {
		this.numberDimensions = numberDimensions;
		this.id = id;
		dimensionArray = new ArrayList<T>();
		while (dimensionArray.size() < numberDimensions) {
			dimensionArray.add(null);
		}
		itemProperties = new HashMap<String, String>();
	}

	/**
	 * Constructor to create a location item with a specified number of dimensions
	 * and a randomly generated unique ID.
	 * 
	 * @param numberDimensions The number of dimensions for the location item.
	 */
	public LocationItemBase(int numberDimensions) {
		this(numberDimensions, UUID.randomUUID().toString());
	}
	
	@Override
	public List<T> getDimensionArray() {
		return dimensionArray;
	}
	
	@Override
	public int getNumberDimensions() {
		return numberDimensions;
	}
	
	@Override
	public void setDim(int dim, T value) throws IllegalArgumentException {
		if (dim < 0 || dim >= numberDimensions) {
			throw new IllegalArgumentException("min dimension 0, max dimension " + numberDimensions + " you entered dim: " + dim + " value: " + value);
		}
		dimensionArray.set(dim, value);
	}
	
	@Override
	public T getDim(int dim) throws IllegalArgumentException {
		if (dim < 0 || dim >= numberDimensions) {
			throw new IllegalArgumentException("min dimension 0, max dimension " + numberDimensions + " you entered dim: " + dim);
		}
		return dimensionArray.get(dim);
	}
	
	@Override
	public String getId() {
		return id;
	}
	
	@Override
	public String getType() {
		return type;
	}

	@Override
	public void setType(String type) {
		this.type = type;
	}

	public abstract JSONObject getJson();
	
	@Override
	public String getProperty(String propertyName) {
		if (propertyName == null) {
			throw new IllegalArgumentException("Property Name must not be null.");
		}
		if (itemProperties.containsKey(propertyName)) {
			return itemProperties.get(propertyName);
		} else {
			return null;
		}
	}

	@Override
	public void setProperty(String propertyName, String propertyValue) {
		if (propertyName == null) {
			throw new IllegalArgumentException("Property Name must not be null.");
		}
		if (propertyValue == null) {
			throw new IllegalArgumentException("Property Value must not be null.");
		}
		itemProperties.put(propertyName, propertyValue);
	}

	@Override
	public boolean containsProperty(String propertyName) {
		if (propertyName == null) {
			throw new IllegalArgumentException("Property Name must not be null.");
		}
		return itemProperties.containsKey(propertyName);
	}
	
	@Override
	public Map<String, String> getProperties() {
		return itemProperties;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getPropertiesJson() {
		
		JSONObject ret = new JSONObject();
		for (String k : itemProperties.keySet()) {
			ret.put(k, itemProperties.get(k));
		}
		
		return ret;
	}
	
	/**
	 * Static Method that calculates the space between two location items.
	 * @param <T> {@link rtree.item.IRType}
	 * @param e1 {@link rtree.item.ILocationItem} 
	 * @param e2 {@link rtree.item.ILocationItem}
	 * @return the space between the two items as a double value.
	 */
	public static <T extends IRType<T>> Double space(ILocationItem<T> e1, ILocationItem<T> e2) {
		
		if (e1.getDimensionArray().size() != e2.getDimensionArray().size()) {
			throw new IllegalArgumentException("Dimensions of items must be the same.");
		}
		
		double spaceTotal = 1; 
		for (int i = 0; i < e1.getDimensionArray().size(); i++) {
			if (e1.getDimensionArray().get(i) == null || e1.getDimensionArray().get(i) == null) {
				throw new IllegalArgumentException("Dimension " + i + " is missing in one of the inputs.");
			}
			
			double d = e1.getDim(i).distanceTo(e2.getDim(i));
	        
			spaceTotal = spaceTotal * d;
			
		}
		
		return spaceTotal;
	}
	
	/**
	 * Static Method that calculates the bounding box between two location items.
	 * 
	 * @param <T> {@link rtree.item.IRType}
	 * @param e1  {@link rtree.item.ILocationItem}
	 * @param e2  {@link rtree.item.ILocationItem}
	 * @return a {@link rtree.item.BoundingBox} object representing the bounding box
	 */
	public static <T extends IRType<T>> BoundingBox getBoundingBox(ILocationItem<T> e1, ILocationItem<T> e2) {
		
		if (e1.getDimensionArray().size() != e2.getDimensionArray().size()) {
			throw new IllegalArgumentException("Dimensions of items must be the same.");
		}
		
		BoundingBox box = new BoundingBox(e1.getNumberDimensions());
		
		for (int i = 0; i < e1.getDimensionArray().size(); i++) {
			if (e1.getDimensionArray().get(i) == null || e1.getDimensionArray().get(i) == null) {
				throw new IllegalArgumentException("Dimension " + i + " is missing in one of the inputs.");
			}
			
			double d = e1.getDim(i).distanceTo(e2.getDim(i));
			
	        box.setDim(i, d);
			
		}
		
		return box;
	}
	
	/**
	 * Static method to convert a list of location items to a JSON array.
	 * 
	 * @param <T>           {@link rtree.item.IRType}
	 * @param locationItems List of {@link rtree.item.ILocationItem} objects
	 * @return JSONArray representation of the location items
	 */
	@SuppressWarnings("unchecked")
	public static <T extends IRType<T>> JSONArray getItemsJSON(List<ILocationItem<T>> locationItems) {
		
		JSONArray arr = new JSONArray();
		if (locationItems != null) {
			for (ILocationItem<T> item : locationItems) {
				
				JSONObject obj = new JSONObject();
				
				for (int i = 0; i < item.getNumberDimensions(); i++) {
					switch (i) {
						case 0: 
							obj.put("x", item.getDim(i).getData().toString());
							break;
						case 1:
							obj.put("y", item.getDim(i).getData().toString());
							break;
						case 2:
							obj.put("z", item.getDim(i).getData().toString());
							break;
						default:
							obj.put("" + i, item.getDim(i).getData().toString());
							break;
					}
					obj.put("type", item.getType());
					obj.put("id", item.getId());
					obj.put("properties", item.getPropertiesJson());
					

				}
				arr.add(obj);
			}
		}
		return arr;
	}
	
}
