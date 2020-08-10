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
public abstract class SplitBehaviorBaseGeneric<T extends IRType<T>> implements ISplitBehaviorGeneric<T> {
	
	protected IRTreeCacheGeneric<T> cache;
	protected int maxChildren;
	protected boolean branchSplit;
	protected String treeName;
	protected ILogger logger;
	
	@Override
	public boolean didBranchSplit() {
		return branchSplit;
	}
	
	@Override
	public IRTreeNodeGeneric<T> getNode(String nodeId) {
		return cache.getNode(nodeId);
	}
	
	
	@Override
	public abstract void splitLeafNode(IRTreeNodeGeneric<T> node, ILocationItemGeneric<T> locationItem);
	@Override
	public abstract void splitBranchNode(IRTreeNodeGeneric<T> node);
	
	
	
	@Override
	public abstract String getDescription();
	
	@Override
	public void initialize(int maxChildren, String treeName, IRTreeCacheGeneric<T> cacheContainer, ILogger logger) {
		this.maxChildren = maxChildren;
		this.treeName = treeName;
		this.cache = cacheContainer;
		this.logger = logger;
	}

}
