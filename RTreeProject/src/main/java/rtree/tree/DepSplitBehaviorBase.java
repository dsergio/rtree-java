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
public abstract class DepSplitBehaviorBase implements DepISplitBehavior {
	
	protected DepRTreeCache cache;
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
	public DepRTreeNode getNode(String nodeId) {
		return cache.getNode(nodeId);
	}
	
	@Override
	public abstract void splitLeafNode2D(DepRTreeNode node, ILocationItem locationItem);
	@Override
	public abstract void splitLeafNodeNDimensional(DepRTreeNode node, ILocationItem locationItem);
	@Override
	public abstract void splitBranchNode2D(DepRTreeNode node);
	@Override
	public abstract void splitBranchNodeNDimensional(DepRTreeNode node);
	@Override
	public abstract String getDescription();
	
	@Override
	public void initialize(int maxChildren, String treeName, DepRTreeCache cacheContainer, ILogger logger) {
		this.maxChildren = maxChildren;
		this.treeName = treeName;
		this.cache = cacheContainer;
		this.logger = logger;
	}

}
