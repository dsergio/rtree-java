package rtree.tree;

import rtree.item.IRType;
import rtree.storage.IDataStorage;

/**
 * R-Tree Cache Interface
 * @param <T> Type of the items stored in the R-tree, extending IRType.
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
	  * @return IDataStorage
	  */
	 IDataStorage<T> getDBAccess();
	 
	 /**
	  * Returns the R-Tree object.
	  * @return IRTree
	  */
	 IRTree<T> getTree();
	 
	 /**
	  * Get a node the cache.
	  * @param nodeId
	  * @return IRTreeNode
	  */
	 IRTreeNode<T> getNode(String nodeId);
	 
	 /**
	  * Remove a node from the cache.
	  * @param nodeId
	  */
	 void removeNode(String nodeId);
	 
	 /**
	  * Put a node in the cache.
	  * @param nodeId
	  * @param node
	  * 
	  */
	 void putNode(String nodeId, IRTreeNode<T> node);
	 
	 /**
	  * Update a node in the cache.
	  * @param nodeId
	  * @param node
	  */
	 void updateNode(String nodeId, IRTreeNode<T> node);
	 
}
