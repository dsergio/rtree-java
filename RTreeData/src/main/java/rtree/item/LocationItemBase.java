package rtree.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public abstract class LocationItemBase<T extends IRType<T>> implements ILocationItem<T> {

	protected List<T> dimensionArray;
	protected int numberDimensions;
	protected Map<String, String> itemProperties;
	protected String type;
	protected final String id;
	
	public LocationItemBase(int numberDimensions, String id) {
		this.numberDimensions = numberDimensions;
		this.id = id;
		dimensionArray = new ArrayList<T>();
		while (dimensionArray.size() < numberDimensions) {
			dimensionArray.add(null);
		}
		itemProperties = new HashMap<String, String>();
	}
	
	public LocationItemBase(int numberDimensions) {
		this(numberDimensions, UUID.randomUUID().toString());
	}
	
	public List<T> getDimensionArray() {
		return dimensionArray;
	}
	
	public int getNumberDimensions() {
		return numberDimensions;
	}
	
	public void setDim(int dim, T value) {
		if (dim < 0 || dim >= numberDimensions) {
			throw new IllegalArgumentException("min dimension 0, max dimension " + numberDimensions + " you entered dim: " + dim + " value: " + value);
		}
		dimensionArray.set(dim, value);
	}
	
	public T getDim(int dim) {
		if (dim < 0 || dim >= numberDimensions) {
			throw new IllegalArgumentException("min dimension 0, max dimension " + numberDimensions + " you entered dim: " + dim);
		}
		return dimensionArray.get(dim);
	}
	
	public String getId() {
		return id;
	}
	
	public String getType() {
		return type;
	}

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
					
					obj.put("properties", item.getPropertiesJson());
					

				}
				arr.add(obj);
			}
		}
		return arr;
	}
	
}
