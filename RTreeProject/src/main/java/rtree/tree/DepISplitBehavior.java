package rtree.tree;

import rtree.item.ILocationItem;
import rtree.log.ILogger;

/**
 * 
 * TODO deprecate
 * 
 * @author David Sergio
 *
 */
public interface DepISplitBehavior {

	public boolean didBranchSplit();
	public DepRTreeNode getNode(String nodeId);
	public void splitLeafNode2D(DepRTreeNode node, ILocationItem locationItem);
	public void splitLeafNodeNDimensional(DepRTreeNode node, ILocationItem locationItem);
	public void splitBranchNode2D(DepRTreeNode node);
	public void splitBranchNodeNDimensional(DepRTreeNode node);
	public String getDescription();
	public void initialize(int maxChildren, String treeName, DepRTreeCache cacheContainer, ILogger logger);

}