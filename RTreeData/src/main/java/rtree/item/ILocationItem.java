package rtree.item;

import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

/**
 * Interface for location items in an R-tree structure.
 * @param <T> {@link rtree.item.IRType}
 */
public interface ILocationItem<T extends IRType<T>> {
	
	/**
	 * Get the ID of the location item.
	 * @return the ID of the location item
	 */
	String getId();
	
	/**
	 * Get the type of the location item.
	 * @return the type of the location item
	 */
	String getType();
	
	/**
	 * Set the type of the location item.
	 * @param type the type to set
	 */
	void setType(String type);
	
	/**
	 * Get the number of dimensions of the location item.
	 * @return the number of dimensions
	 */
	int getNumberDimensions();
	
	/**
	 * Get the JSON representation of the location item.
	 * @return a {@link org.json.simple.JSONObject} representing the location item
	 */
	JSONObject getJson();
	
	/**
	 * Set the value for a specific dimension.
	 * @param dim Dimension. 0 for "x", 1 for "y", 2 for "z", etc.
	 * @param value value to set for the dimension. See {@link rtree.item.IRType} for the type of value.
	 */
	void setDim(int dim, T value);
	
	/**
	 * Get the value for a specific dimension.
	 * @param dim Dimension. 0 for "x", 1 for "y", 2 for "z", etc.
	 * @return value for the dimension. See {@link rtree.item.IRType} for the type of value.
	 */
	T getDim(int dim);
	
	/**
	 * Get the array of dimensions.
	 * @return a list of dimensions, each of type {@link rtree.item.IRType}
	 */
	List<T> getDimensionArray();
	
	/**
	 * Get the JSON representation of the location item.
	 * @return a {@link org.json.simple.JSONObject} containing the location item data
	 */
	JSONObject getLocationJson();
	
	/**
	 * Get the value of a specific property.
	 * @param propertyName the name of the property
	 * @return the value of the property, or null if it does not exist
	 */
	String getProperty(String propertyName);
	
	/**
	 * Set a property for the location item.
	 * @param propertyName the name of the property
	 * @param propertyValue the value of the property
	 */
	void setProperty(String propertyName, String propertyValue);
	
	/**
	 * Check if the location item contains a specific property.
	 * @param propertyName the name of the property to check
	 * @return true if the property exists, false otherwise
	 */
	boolean containsProperty(String propertyName);
	
	/**
	 * Get the properties of the location item as a JSON object.
	 * @return a {@link org.json.simple.JSONObject} containing the properties
	 */
	JSONObject getPropertiesJson();
	
	/**
	 * Get the properties of the location item as a map.
	 * @return a map containing the properties, where keys are property names and values are property values
	 */
	Map<String, String> getProperties();
	
}
