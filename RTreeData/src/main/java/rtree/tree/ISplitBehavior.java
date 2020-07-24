package rtree.tree;

import rtree.item.ILocationItem;
import rtree.log.ILogger;

/**
 * 
 * @author David Sergio
 *
 */
public interface ISplitBehavior {

	 boolean didBranchSplit();
	 IRTreeNode getNode(String nodeId);
	
	 void splitLeafNode(IRTreeNode node, ILocationItem locationItem);
	 void splitBranchNode(IRTreeNode node);
	
	 String getDescription();
	 void initialize(int maxChildren, String treeName, IRTreeCache cacheContainer, ILogger logger);

}