package rtree.tree;

import rtree.item.ILocationItem;
import rtree.log.ILogger;

/**
 * 
 * @author David Sergio
 *
 */
public interface ISplitBehavior {

	public boolean didBranchSplit();
	public IRTreeNode getNode(String nodeId);
	
	public void splitLeafNode(IRTreeNode node, ILocationItem locationItem);
	public void splitBranchNode(IRTreeNode node);
	
	public String getDescription();
	public void initialize(int maxChildren, String treeName, IRTreeCache cacheContainer, ILogger logger);

}