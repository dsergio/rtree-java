package rtree.item.generic;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import rtree.item.BoundingBox;
import rtree.item.LocationItem2D;

public abstract class LocationItemBaseGeneric<T extends IRType<T>> implements ILocationItemGeneric<T> {

	protected List<T> dimensionArray;
	protected int numberDimensions;
	protected Object data;
	protected String type;
	protected final String id;
	
	public LocationItemBaseGeneric(int numberDimensions, String id) {
		this.numberDimensions = numberDimensions;
		this.id = id;
		dimensionArray = new ArrayList<T>();
		while (dimensionArray.size() < numberDimensions) {
			dimensionArray.add(null);
		}
	}
	
	public LocationItemBaseGeneric(int numberDimensions) {
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
	
	public static <T extends IRType<T>> Double space(ILocationItemGeneric<T> e1, ILocationItemGeneric<T> e2) {
		
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
	
	public static <T extends IRType<T>> BoundingBox getBoundingBox(ILocationItemGeneric<T> e1, ILocationItemGeneric<T> e2) {
		
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
	public static <T extends IRType<T>> JSONArray getItemsJSON(List<ILocationItemGeneric<T>> locationItems) {
		
		JSONArray arr = new JSONArray();
		if (locationItems != null) {
			for (ILocationItemGeneric<T> item : locationItems) {
				
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

				}
				arr.add(obj);
			}
		}
		return arr;
	}
	
	public static List<LocationItem2D> getLocationItemListFromJson2D(String items) {
		
		JSONParser parser = new JSONParser();
		Object obj;
		List<LocationItem2D> locationItems = new ArrayList<LocationItem2D>();
		
		try {
			if (items != null && !items.equals("") && !items.equals("delete")) {
				
				obj = parser.parse(items);
				JSONArray arr = (JSONArray) obj;
				for (int i = 0; i < arr.size(); i++) {
					JSONObject row = (JSONObject) arr.get(i);
					LocationItem2D item = new LocationItem2D(Integer.parseInt(row.get("x").toString()), Integer.parseInt(row.get("y").toString()), row.get("type").toString());
					locationItems.add(item);
				}
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return locationItems;
	}
}
