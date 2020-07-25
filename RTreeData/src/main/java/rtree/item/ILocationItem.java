package rtree.item;

import java.util.List;

import org.json.simple.JSONObject;

/**
 * 
 * R-Tree Location Item Type Interface
 * 
 * @author David Sergio
 *
 */
public interface ILocationItem {
	
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
	 void setDim(int dim, int value);
	
	/**
	 * 
	 * @param dim Dimension. 0 for "x", 1 for "y", 2 for "z", etc.
	 * @return value
	 */
	 Integer getDim(int dim);
	 
	 /**
	  * 0 for "x", 1 for "y", 2 for "z", etc.
	  * 
	  * @return Dimension Array
	  */
	 List<Integer> getDimensionArray();
	 
	 JSONObject getLocationJson();
	
	
	

}
