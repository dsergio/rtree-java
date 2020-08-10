package rtree.tree.generic;

import rtree.item.ILocationItem;
import rtree.item.generic.ILocationItemGeneric;
import rtree.item.generic.IRType;
import rtree.log.ILogger;

/**
 * 
 * @author David Sergio
 *
 */
public interface ISplitBehaviorGeneric<T extends IRType<T>> {

	 boolean didBranchSplit();
	 IRTreeNodeGeneric<T> getNode(String nodeId);
	
	 void splitLeafNode(IRTreeNodeGeneric<T> node, ILocationItemGeneric<T> locationItem);
	 void splitBranchNode(IRTreeNodeGeneric<T> node);
	
	 String getDescription();
	 void initialize(int maxChildren, String treeName, IRTreeCacheGeneric<T> cacheContainer, ILogger logger);

}