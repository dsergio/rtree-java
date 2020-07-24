package rtree.rectangle;

import java.util.List;

import org.json.simple.JSONObject;

import rtree.item.ILocationItem;

public interface IHyperRectangle {
	
	/**
	 * 
	 * @param dim Dimension. 0 for "x", 1 for "y", 2 for "z", etc.
	 * @param value
	 */
	 void setDim1(int dim, Integer value);
	
	/**
	 * 
	 * @param dim Dimension. 0 for "x", 1 for "y", 2 for "z", etc.
	 * @param value
	 */
	 void setDim2(int dim, Integer value);
	
	/**
	 * 
	 * @param dim Dimension. 0 for "x", 1 for "y", 2 for "z", etc.
	 * @return
	 */
	 Integer getDim1(int dim);
	
	/**
	 * 
	 * @param dim Dimension. 0 for "x", 1 for "y", 2 for "z", etc.
	 * @return
	 */
	 Integer getDim2(int dim);
	
	 List<Integer> getDimensionArray1();
	 List<Integer> getDimensionArray2();
	 int getNumberDimensions();
	 JSONObject getJson();
	 boolean containsPoint(ILocationItem item);
	 int getSpace();
	 int getLevel();
	 void setLevel(int level);
	
	
}
