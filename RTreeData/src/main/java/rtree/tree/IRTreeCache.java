package rtree.tree;

import rtree.item.IRType;
import rtree.storage.IDataStorage;

/**
 * R-Tree Cache Interface
 * @param <T> {@link rtree.item.IRType}
 * @author David Sergio
 *
 */
public interface IRTreeCache<T extends IRType<T>> {
	
	/**
	 * Prints the cache contents to the logger.
	 * 
	 */
	 void printCache();
	 
	 /**
	  * Returns the data storage access object.
	  * @return the IDataStorage object associated with this cache
	  */
	 IDataStorage<T> getDBAccess();
	 
	 /**
	  * Returns the R-Tree object.
	  * @return the IRTree object associated with this cache
	  */
	 IRTree<T> getTree();
	 
	 /**
	  * Get a node the cache.
	  * @param nodeId the ID of the node to retrieve
	  * @return the IRTreeNode associated with the given nodeId, or null if not found
	  */
	 IRTreeNode<T> getNode(String nodeId);
	 
	 /**
	  * Remove a node from the cache.
	  * @param nodeId the ID of the node to remove
	  */
	 void removeNode(String nodeId);
	 
	 /**
	  * Put a node in the cache.
	  * @param nodeId the ID of the node to put in the cache
	  * @param node the IRTreeNode to be cached
	  * 
	  */
	 void putNode(String nodeId, IRTreeNode<T> node);
	 
	 /**
	  * Update a node in the cache.
	  * @param nodeId the ID of the node to update
	  * @param node the IRTreeNode to update in the cache
	  */
	 void updateNode(String nodeId, IRTreeNode<T> node);
	 
}
