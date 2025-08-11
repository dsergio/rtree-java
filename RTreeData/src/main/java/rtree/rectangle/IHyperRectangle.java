package rtree.rectangle;

import java.util.List;

import org.json.simple.JSONObject;

import rtree.item.ILocationItem;
import rtree.item.IRType;

/**
 * IHyperRectangle interface represents a hyper-rectangle in an R-tree structure.
 * @param <T> {@link rtree.item.IRType} representing the type of dimensions
 */
public interface IHyperRectangle<T extends IRType<T>> {
	
	/**
	 * Set the value for a specific dimension 1.
	 * @param dim Dimension. 0 for "x", 1 for "y", 2 for "z", etc.
	 * @param value value for the dimension. See {@link rtree.item.IRType} for the type of value.
	 * @throws IllegalArgumentException if the dimension is invalid or the value is null
	 */
	 void setDim1(int dim, T value) throws IllegalArgumentException;
	
	/**
	 * Set the value for a specific dimension 2.
	 * @param dim Dimension. 0 for "x", 1 for "y", 2 for "z", etc.
	 * @param value value for the dimension. See {@link rtree.item.IRType} for the type of value.
	 * throws IllegalArgumentException if the dimension is invalid or the value is null
	 */
	 void setDim2(int dim, T value) throws IllegalArgumentException;
	
	/**
	 * Get the value for a specific dimension 1.
	 * @param dim Dimension. 0 for "x", 1 for "y", 2 for "z", etc.
	 * @return value for the dimension. See {@link rtree.item.IRType} for the type of value.
	 * @throws IllegalArgumentException if the dimension is invalid
	 */
	 T getDim1(int dim) throws IllegalArgumentException;
	
	/**
	 * Get the value for a specific dimension 2.
	 * @param dim Dimension. 0 for "x", 1 for "y", 2 for "z", etc.
	 * @return value for the dimension. See {@link rtree.item.IRType} for the type of value.
	 * @throws IllegalArgumentException if the dimension is invalid
	 */
	 T getDim2(int dim) throws IllegalArgumentException;
	
	 /**
	  * Get List of dimension 1 values.
	  * @return List of dimension 1 values, each of type {@link rtree.item.IRType}
	  */
	 List<T> getDimensionArray1();
	 
	 /**
	  * Get List of dimension 2 values.
	  * @return List of dimension 2 values, each of type {@link rtree.item.IRType}
	  */
	 List<T> getDimensionArray2();
	 
	 /**
	  * Get the ID of the hyper-rectangle.
	  * @return the ID of the hyper-rectangle
	  */
	 int getNumberDimensions();
	 
	 /**
	  * Get the JSON representation of the hyper-rectangle.
	  * @return a string representation of the hyper-rectangle
	  */
	 JSONObject getJson();
	 
	 /**
	  * Check if the hyper-rectangle contains a specific location item.
	  * @param item {@link rtree.item.ILocationItem} to check
	  * @return true if the hyper-rectangle contains the location item, false otherwise
	  */
	 boolean containsPoint(ILocationItem<T> item);
	 
	 /**
	  * Get the space of the hyper-rectangle.
	  * @return the space of the hyper-rectangle
	  */
	 double getSpace();
	 
	 /**
	  * Get the level of the hyper-rectangle in the R-tree structure.
	  * @return the level of the hyper-rectangle
	  */
	 int getLevel();
	 
	 /**
	  * Set the level of the hyper-rectangle in the R-tree structure.
	  * @param level the level to set for the hyper-rectangle
	  */
	 void setLevel(int level);
	
}
