package rtree.rectangle.generic;

import java.util.List;

import org.json.simple.JSONObject;

import rtree.item.ILocationItem;
import rtree.item.generic.ILocationItemGeneric;
import rtree.item.generic.IRType;

public interface IHyperRectangleGeneric<T extends IRType<T>> {
	
	/**
	 * 
	 * @param dim Dimension. 0 for "x", 1 for "y", 2 for "z", etc.
	 * @param value
	 */
	 void setDim1(int dim, T value);
	
	/**
	 * 
	 * @param dim Dimension. 0 for "x", 1 for "y", 2 for "z", etc.
	 * @param value
	 */
	 void setDim2(int dim, T value);
	
	/**
	 * 
	 * @param dim Dimension. 0 for "x", 1 for "y", 2 for "z", etc.
	 * @return
	 */
	 T getDim1(int dim);
	
	/**
	 * 
	 * @param dim Dimension. 0 for "x", 1 for "y", 2 for "z", etc.
	 * @return
	 */
	 T getDim2(int dim);
	
	 List<T> getDimensionArray1();
	 List<T> getDimensionArray2();
	 int getNumberDimensions();
	 JSONObject getJson();
	 boolean containsPoint(ILocationItemGeneric<T> item);
	 double getSpace();
	 int getLevel();
	 void setLevel(int level);
	
	
}
