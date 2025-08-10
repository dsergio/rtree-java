package rtree.tree;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

import rtree.item.ILocationItem;
import rtree.item.IRType;
import rtree.log.PerformanceMetrics;
import rtree.rectangle.IHyperRectangle;

/**
 * Interface for R-Tree data structure.
 * @param <T> {@link rtree.item.IRType}
 */
public interface IRTree<T extends IRType<T>> {
	 
	/**
	 * Insert a location item into the R-tree
	 * @param locationItem {@link rtree.item.ILocationItem} to insert
	 * @throws IOException if an error occurs during insertion
	 */
	 void insert(ILocationItem<T> locationItem) throws IOException;
	 
	 /**
	  * Insert a random type of "animal" into the R-tree
	  * 
	  * @param locationItem {@link rtree.item.ILocationItem} to insert, with the type automatically set to a random animal type.
	  * @throws IOException if an error occurs during insertion
	  */
	 void insertRandomAnimal(ILocationItem<T> locationItem) throws IOException;
	 
	 /**
	  * Insert a random type of "city" into the R-tree
	  * @param locationItem {@link rtree.item.ILocationItem} to insert, with the type automatically set to a random city type.
	  * @throws IOException if an error occurs during insertion
	  */
	 void insertRandomWACity(ILocationItem<T> locationItem) throws IOException;
	 
	 /**
	  * Deletes the RTree and all its data.
	  * @throws Exception if an error occurs during deletion
	  */
	 void delete() throws Exception;
	 
	 /**
	 * Get all items in this tree
	 * Caution: could be a big operation
	 * @return List of all {@link rtree.item.ILocationItem} in this tree
	 * 
	 */
	 List<ILocationItem<T>> getAllLocationItems();
	 
	 /**
	  * Get all items in this tree with their depth in the tree.
	  * @return a map of {@link rtree.item.ILocationItem} with their depth in the tree.
	  */
	 Map<ILocationItem<T>, Integer> getAllLocationItemsWithDepth();
	 
	 /**
	  * Get all rectangles in this tree
	  * @return all rectangles in this tree
	  */
	 List<IHyperRectangle<T>> getAllRectangles();
	 
	 /**
	  * Get all rectangles in this tree with their depth in the tree.
	  * @return a map of rectangles with their depth in the tree.
	  */
	 Map<IHyperRectangle<T>, Integer> getAllRectanglesWithDepth();
	 
	 /**
	  * Delete a specific location item from the R-tree.
	  * @param locationItem {@link rtree.item.ILocationItem} to delete
	  */
	 void delete(ILocationItem<T> locationItem);
	 
	/**
	 * Query the R-Tree structure and retrieve the items that fall inside the parameter search rectangle
	 * @param searchRectangle the rectangle to search for items
	 * @return a map of rectangles containing search results
	 * 
	 */
	 Map<IHyperRectangle<T>, List<ILocationItem<T>>> search(IHyperRectangle<T> searchRectangle);
	 
	 /**
	  * Print tree
	  * 
	  */
	 void printTree();
	 
	 /**
	  * Get the name of the tree
	  * @return the name of the tree
	  */
	 String getTreeName();
	 
	 /** 
	  * get maximum number of children per node
	  * @return maximum number of children per node
	  */
	 int getMaxChildren();
	 
	 /**
	  * get maximum number of items per node
	  * @return maximum number of items per node
	  */
	 int getMaxItems();
	 
	 /**
	  * Get the JSON representation of the R-tree
	  * @return a JSONObject containing the R-tree structure
	  */
	 JSONObject getJSON();
	 
	 /**
	  * Get the number of dimensions of the R-tree
	  * @return the number of dimensions
	  */
	 int getNumDimensions();
	 
	 /**
	 * Get the minimum of each dimension in the R-tree.
	 * @return a list of minimum values for each dimension
	 */
	 List<T> getMin();
	 
	 /**
	  * Get the maximum of each dimension in the R-tree.
	  * @return a list of maximum values for each dimension
	  */
	 List<T> getMax();
	 
	 /**
	  * Get the performance metrics of the R-tree.
	  * @return the performance metrics
	  */
	 PerformanceMetrics getPerformance();
	 
}


