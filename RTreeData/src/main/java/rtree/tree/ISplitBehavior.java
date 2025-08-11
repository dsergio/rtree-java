package rtree.tree;

import rtree.item.ILocationItem;
import rtree.item.IRType;
import rtree.log.ILogger;

/**
 * Interface for R-Tree split behavior.
 * 
 * @param <T> {@link rtree.item.IRType}
 *
 */
public interface ISplitBehavior<T extends IRType<T>> {

	/**
	 * Get whether a branch split has occurred.
	 * @return true if a branch split has occurred, false otherwise
	 */
	boolean didBranchSplit();
	
	/**
	 * Get the node with the specified ID.
	 * 
	 * @param nodeId the ID of the node to retrieve
	 * @return the IRTreeNode associated with the given nodeId, or null if not found
	 */
	IRTreeNode<T> getNode(String nodeId);
	
	/**
	 * Split a leaf node.
	 * 
	 * @param node         the leaf node to split
	 * @param locationItem the location item to be added to the split nodes
	 */
	void splitLeafNode(IRTreeNode<T> node, ILocationItem<T> locationItem);
	 
	/**
	 * Split a branch node.
	 * 
	 * @param node the branch node to split
	 */
	void splitBranchNode(IRTreeNode<T> node);
	
	/**
	 * Get the description of the split behavior.
	 * 
	 * @return a string description of the split behavior
	 */
	String getDescription();
	 
	/**
	 * Initialize the split behavior with the necessary parameters.
	 * 
	 * @param maxChildren    the maximum number of children allowed in a node
	 * @param treeName       the name of the R-tree
	 * @param cacheContainer the cache container for R-tree nodes
	 * @param logger         the logger for logging operations
	 */
	void initialize(int maxChildren, String treeName, IRTreeCache<T> cacheContainer, ILogger logger);

}