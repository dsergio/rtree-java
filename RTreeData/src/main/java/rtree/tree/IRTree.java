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
 * @param <T> Type of the items stored in the R-tree, extending IRType.
 * @author David Sergio
 * 
 */
public interface IRTree<T extends IRType<T>> {
	 
	/**
	 * Insert a location item into the R-tree
	 * @param locationItem
	 * 
	 */
	 void insert(ILocationItem<T> locationItem) throws IOException;
	 
	 /**
	  * Insert a random type of "animal" into the R-tree
	  * 
	  * @param locationItem
	  * 
	  */
	 void insertRandomAnimal(ILocationItem<T> locationItem) throws IOException;
	 
	 /**
	  * Insert a random type of "city" into the R-tree
	  * @param locationItem
	  * @throws IOException
	  */
	 void insertRandomWACity(ILocationItem<T> locationItem) throws IOException;
	 
	 /**
	  * Deletes the RTree and all its data.
	  * @throws Exception
	  */
	 void delete() throws Exception;
	 
	 /**
	 * Get all items in this tree
	 * Caution: could be a big operation
	 * @return all items in this tree
	 * 
	 */
	 List<ILocationItem<T>> getAllLocationItems();
	 
	 /**
	  * Get all items in this tree with their depth in the tree.
	  * @return a map of items with their depth in the tree.
	  */
	 Map<ILocationItem<T>, Integer> getAllLocationItemsWithDepth();
	 
	 /**
	  * Get all rectangles in this tree
	  * @return
	  */
	 List<IHyperRectangle<T>> getAllRectangles();
	 
	 /**
	  * Get all rectangles in this tree with their depth in the tree.
	  * @return
	  */
	 Map<IHyperRectangle<T>, Integer> getAllRectanglesWithDepth();
	 
	 /**
	  * Delete a specific location item from the R-tree.
	  * @param toDelete
	  */
	 void delete(ILocationItem<T> toDelete);
	 
	/**
	 * Query the R-Tree structure and retrieve the items that fall inside the parameter search rectangle
	 * @param searchRectangle
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
	  * @return
	  */
	 PerformanceMetrics getPerformance();
	 
}


