package rtree.item.generic;

import java.util.List;

import org.json.simple.JSONObject;

public interface ILocationItemGeneric<T extends IRType<T>> {
	
	String getId();
	String getType();
	void setType(String type);
	int getNumberDimensions();
	JSONObject getJson();
	
	/**
	 * 
	 * @param dim Dimension. 0 for "x", 1 for "y", 2 for "z", etc.
	 * @param value
	 */
	void setDim(int dim, T value);
	
	/**
	 * 
	 * @param dim Dimension. 0 for "x", 1 for "y", 2 for "z", etc.
	 * @return value
	 */
	T getDim(int dim);
	List<T> getDimensionArray();
	JSONObject getLocationJson();
	
	String getProperty(String propertyName);
	void setProperty(String propertyName, String propertyValue);
	boolean containsProperty(String propertyName);
	JSONObject getPropertiesJson();
}
