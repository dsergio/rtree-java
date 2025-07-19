package rtree.tree;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

import rtree.item.ILocationItem;
import rtree.item.IRType;
import rtree.rectangle.IHyperRectangle;

public interface IRTree<T extends IRType<T>> {
	 
	/**
	 * Insert a location item into the R-tree
	 * 
	 * @param LocationItem locationItem
	 * 
	 */
	 void insert(ILocationItem<T> locationItem) throws IOException;
	 
	 /**
	  * Insert a random type of "animal" into the R-tree
	  * 
	  * @param LocationItem locationItem
	  * 
	  */
	 void insertRandomAnimal(ILocationItem<T> locationItem) throws IOException;
	 
	 /**
	  * Deletes the RTree and all its data.
	  * @param treeName
	  * @throws Exception
	  */
	 void delete() throws Exception;
	 
	 /**
	 * Get all items in this tree
	 * Caution: could be a big operation
	 * 
	 * @return all items in this tree
	 * 
	 */
	 List<ILocationItem<T>> getAllPoints();
	 
	 /**
	  * Get all rectangles in this tree
	  * @return
	  */
	 List<IHyperRectangle<T>> getAllRectangles();
	 
	 
	 /**
	  * Delete a specific location item from the R-tree.
	  * @param toDelete
	  */
	 void delete(ILocationItem<T> toDelete);
	 
	/**
	 * Query the R-Tree structure and retrieve the items that fall inside the parameter search rectangle
	 * 
	 * @param HyperRectangle searchRectangle
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
	 
	 
	 // the rest of these methods are internal, and should be moved out of this public interface
	 //
	 IRTreeCache<T> getCache();
	 void updateRoot();
	 boolean metaDataExists() throws Exception;
	 int getMaxChildren();
	 int getMaxItems();
	 IRTreeNode<T> getNode(String nodeId);
	 void addNode(String nodeId, String children, String parent, String items, String rectangle, IRTreeNode<T> node);
	 void addNode(String nodeId, String children, String parent, String items, String rectangle);
	 Map<ILocationItem<T>, Integer> getPointsWithDepth();
	 JSONObject getJson();
	 int numAdds();
	 int numReads();
	 int numUpdates();
	 long getAddTime();
	 long getReadTime();
	 long getUpdateTime();
	 int getNumDimensions();
	 List<T> getMin();
	 List<T> getMax();
}


