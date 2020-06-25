package rtree.tree;

import rtree.item.ILocationItem;
import rtree.log.ILogger;

/**
 * 
 * Split Behavior base class
 * 
 * @author David Sergio
 *
 */
public abstract class SplitBehaviorBase implements ISplitBehavior {
	
	protected IRTreeCache cache;
	protected int maxChildren;
	protected boolean branchSplit;
//	protected RTreeNode root;
	protected String treeName;
	protected ILogger logger;
	
	@Override
	public boolean didBranchSplit() {
		return branchSplit;
	}
	
	@Override
	public IRTreeNode getNode(String nodeId) {
		return cache.getNode(nodeId);
	}
	
//	@Override
//	public abstract void splitLeafNode2D(IRTreeNode node, ILocationItem locationItem);
//	@Override
//	public abstract void splitLeafNodeNDimensional(IRTreeNode node, ILocationItem locationItem);
//	@Override
//	public abstract void splitBranchNode2D(IRTreeNode node);
//	@Override
//	public abstract void splitBranchNodeNDimensional(IRTreeNode node);
	
	@Override
	public abstract void splitLeafNode(IRTreeNode node, ILocationItem locationItem);
	@Override
	public abstract void splitBranchNode(IRTreeNode node);
	
	
	
	@Override
	public abstract String getDescription();
	
	@Override
	public void initialize(int maxChildren, String treeName, IRTreeCache cacheContainer, ILogger logger) {
		this.maxChildren = maxChildren;
		this.treeName = treeName;
		this.cache = cacheContainer;
		this.logger = logger;
	}

}
