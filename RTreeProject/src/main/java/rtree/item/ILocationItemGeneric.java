package rtree.item;

import java.util.List;

import org.json.simple.JSONObject;

public interface ILocationItemGeneric<T extends IRType<T>> {
	
	public String getType();
	public void setType(String type);
	public int getNumberDimensions();
	public JSONObject getJson();
	
	/**
	 * 
	 * @param dim Dimension. 0 for "x", 1 for "y", 2 for "z", etc.
	 * @param value
	 */
	public void setDim(int dim, T value);
	
	/**
	 * 
	 * @param dim Dimension. 0 for "x", 1 for "y", 2 for "z", etc.
	 * @return value
	 */
	public T getDim(int dim);
	public List<T> getDimensionArray();
	
	
	

}
