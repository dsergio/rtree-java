package cloudrtree;

import java.util.List;

import org.json.simple.JSONObject;

public interface ILocationItem {
	
	public String getType();
	public void setType(String type);
	public int getNumberDimensions();
	public JSONObject getJson();
	
	/**
	 * 
	 * @param dim Dimension. 0 for "x", 1 for "y", 2 for "z", etc.
	 * @param value
	 */
	public void setDim(int dim, int value);
	
	/**
	 * 
	 * @param dim Dimension. 0 for "x", 1 for "y", 2 for "z", etc.
	 * @return value
	 */
	public Integer getDim(int dim);
	public List<Integer> getDimensionArray();
	
	
	

}
