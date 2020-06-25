package rtree.tree;

import rtree.item.ILocationItem;
import rtree.log.ILogger;

public interface ISplitBehavior {

	public boolean didBranchSplit();
	public IRTreeNode getNode(String nodeId);
	
//	public void splitLeafNode2D(IRTreeNode node, ILocationItem locationItem);
//	public void splitLeafNodeNDimensional(IRTreeNode node, ILocationItem locationItem);
//	public void splitBranchNode2D(IRTreeNode node);
//	public void splitBranchNodeNDimensional(IRTreeNode node);
	
	public void splitLeafNode(IRTreeNode node, ILocationItem locationItem);
	public void splitBranchNode(IRTreeNode node);
	
	public String getDescription();
	public void initialize(int maxChildren, String treeName, IRTreeCache cacheContainer, ILogger logger);

}