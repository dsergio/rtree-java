package rtree.storage;

import java.util.List;

import rtree.item.ILocationItem;
import rtree.item.IRType;
import rtree.log.PerformanceMetrics;
import rtree.tree.IRTreeCache;
import rtree.tree.IRTree;
import rtree.tree.IRTreeNode;

/**
 * Storage Access Interface
 * @param <T> Type of the items stored in the R-tree, extending IRType.
 */
public interface IDataStorage<T extends IRType<T>> {
	
	/**
	 * Get all R-trees stored in the database.
	 * 
	 * @return List of R-trees.
	 */
	List<IRTree<T>> getAllTrees();
	
	/**
	 * Get all location items stored in the database.
	 * 
	 * @return List of location items.
	 */
	List<ILocationItem<T>> getAllLocationItems();
	
	/**
	 * Get a list of minimum values, the size of which is the number of dimensions for the specified R-tree.
	 * 
	 * @param treeName Name of the R-tree.
	 * @return List of minimum values for the specified R-tree.
	 */
	List<T> getMin(String treeName);
	
	/**
	 * Get a list of maximum values, the size of which is the number of dimensions
	 * for the specified R-tree.
	 * 
	 * @param treeName Name of the R-tree.
	 * @return List of maximum values for the specified R-tree.
	 */
	List<T> getMax(String treeName);
	
	/**
	 * Add an R-tree node to the database.
	 * 
	 * @param nodeId ID of the R-tree node.
	 * @param children IDs of the children nodes, comma-separated.
	 * @param parent ID of the parent node.
	 * @param items IDs of the items, comma-separated.
	 * @param rectangle HyperRectangle in JSON format.
	 * @param treeName Name of the R-tree to which the node belongs.
	 * @param cache Cache for the R-tree nodes.
	 * @return The created R-tree node.
	 */
	IRTreeNode<T> addRTreeNode(String nodeId, String children, String parent, String items, String rectangle, String treeName, IRTreeCache<T> cache);
	
	/**
	 * Get an R-tree node from the database.
	 * 
	 * @param treeName Name of the R-tree.
	 * @param nodeId   ID of the R-tree node.
	 * @param cache    Cache for the R-tree nodes.
	 * @return The retrieved R-tree node.
	 */
	IRTreeNode<T> getRTreeNode(String treeName, String nodeId, IRTreeCache<T> cache);
	
	/**
	 * Update an R-tree node in the database.
	 * 
	 * @param treeName Name of the R-tree.
	 * @param nodeId ID of the R-tree node.
	 * @param children IDs of the children nodes, comma-separated.
	 * @param parent ID of the parent node.
	 * @param items IDs of the items, comma-separated.
	 * @param rectangle HyperRectangle in JSON format.
	 */
	void updateRTreeNode(String treeName, String nodeId, String children, String parent, String items, String rectangle);
	
	/**
	 * Add a location item to the database.
	 * 
	 * @param Id          Unique identifier for the location item.
	 * @param N           Number of dimensions for the R-tree.
	 * @param location   Location in JSON format.
	 * @param type       Type of the location item.
	 * @param properties Additional properties of the location item in JSON format.
	 */
	void addLocationItem(String Id, int N, String location, String type, String properties);
	
	/**
	 * Get performance metrics for the data storage operations.
	 * 
	 * @return PerformanceMetrics object containing performance data.
	 */
	PerformanceMetrics getPerformance();
	
	// metadata methods
	
	/**
	 * Add metadata for an R-tree.
	 * @param treeName Name of the R-tree.
	 * @param maxChildren maximum number of children for the R-tree nodes.
	 * @param maxItems maximum number of items for the R-tree nodes.
	 * @param N number of dimensions for the R-tree.
	 */
	void addToMetaData(String treeName, int maxChildren, int maxItems, int N); // store N, maxChildren and maxItems values
	
	/**
	 * Get whether metadata exists for a given R-tree name.
	 * @param treeName Name of the R-tree.
	 * @return true if metadata exists for the specified R-tree, false otherwise.
	 * @throws Exception if an error occurs while checking metadata existence.
	 */
	boolean metaDataExists(String treeName) throws Exception; // if treeName exists, use the metadata preferentially
	 
	/**
	 * Get Max Children value for a given R-tree name.
	 * @param treeName Name of the R-tree.
	 * @return the maximum number of children for the R-tree nodes.
	 */
	int getMaxChildren(String treeName); // get the persistent maxChildren value
	
	/**
	 * Get Max Items value for a given R-tree name.
	 * 
	 * @param treeName Name of the R-tree.
	 * @return the maximum number of items for the R-tree nodes.
	 */
	int getMaxItems(String treeName);  // get the persistent maxItems value
	
	/**
	 * Get the number of dimensions for a given R-tree name.
	 * 
	 * @param treeName Name of the R-tree.
	 * @return the number of dimensions for the R-tree.
	 */
	int getNumDimensions(String treeName);  // get number of dimensions
	
	/**
	 * Update the metadata boundaries for a given R-tree.
	 * @param minimums list of minimum values for each dimension.
	 * @param maximums list of maximum values for each dimension.
	 * @param treeName Name of the R-tree.
	 */
	void updateMetaDataBoundaries(List<T> minimums, List<T> maximums, String treeName);
	 
	// storage management methods
	
	/**
	 * Initialize the data storage. This method should be called to set up the
	 * storage before performing any operations.
	 */
	void init();
	
	/**
	 * Initialize the storage for the R-tree. This method should be called before
	 * any other operations on the storage.
	 */
	void initializeStorage();
	
	/**
	 * Close the storage connection. This method should be called when the storage
	 * is no longer needed to release resources.
	 */
	void close();
	
	/**
	 * Get the storage type of the data storage.
	 * 
	 * @return StorageType enum representing the type of storage (e.g., MySQL,
	 *         SQLite).
	 */
	StorageType getStorageType();
	
	/**
	 * Clear all data from the storage. This method should be used with caution as
	 * it will remove all stored R-trees and items.
	 */
	void clearData();
	
	/**
	 * Check if the database connection is established.
	 * 
	 * @return true if the database connection is active, false otherwise.
	 */
	boolean isDbConnected();
	 
}

