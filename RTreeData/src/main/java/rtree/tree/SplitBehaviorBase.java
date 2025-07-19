package rtree.tree;

import rtree.item.ILocationItem;
import rtree.item.IRType;
import rtree.log.ILogger;

/**
 * 
 * @author David Sergio
 *
 */
public abstract class SplitBehaviorBase<T extends IRType<T>> implements ISplitBehavior<T> {
	
	protected IRTreeCache<T> cache;
	protected int maxChildren;
	protected boolean branchSplit;
	protected String treeName;
	protected ILogger logger;
	
	@Override
	public boolean didBranchSplit() {
		return branchSplit;
	}
	
	@Override
	public IRTreeNode<T> getNode(String nodeId) {
		return cache.getNode(nodeId);
	}
	
	
	@Override
	public abstract void splitLeafNode(IRTreeNode<T> node, ILocationItem<T> locationItem);
	@Override
	public abstract void splitBranchNode(IRTreeNode<T> node);
	
	
	
	@Override
	public abstract String getDescription();
	
	@Override
	public void initialize(int maxChildren, String treeName, IRTreeCache<T> cacheContainer, ILogger logger) {
		this.maxChildren = maxChildren;
		this.treeName = treeName;
		this.cache = cacheContainer;
		this.logger = logger;
	}

}
