package rtree.tree;

import rtree.item.ILocationItem;
import rtree.item.IRType;
import rtree.log.ILogger;

/**
 * Interface for R-Tree split behavior.
 * 
 * @param <T> Type of the items stored in the R-tree, extending IRType.
 * @author David Sergio
 *
 */
public interface ISplitBehavior<T extends IRType<T>> {

	 boolean didBranchSplit();
	 IRTreeNode<T> getNode(String nodeId);
	
	 void splitLeafNode(IRTreeNode<T> node, ILocationItem<T> locationItem);
	 void splitBranchNode(IRTreeNode<T> node);
	
	 String getDescription();
	 void initialize(int maxChildren, String treeName, IRTreeCache<T> cacheContainer, ILogger logger);

}