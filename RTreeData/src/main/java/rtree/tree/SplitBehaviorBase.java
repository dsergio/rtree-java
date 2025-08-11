package rtree.tree;

import rtree.item.ILocationItem;
import rtree.item.IRType;
import rtree.log.ILogger;

/**
 * Abstract base class for R-Tree split behavior.
 * @param <T> {@link rtree.item.IRType}
 *
 */
public abstract class SplitBehaviorBase<T extends IRType<T>> implements ISplitBehavior<T> {
	
	/**
	 * The cache for R-tree nodes.
	 */
	protected IRTreeCache<T> cache;
	
	/**
	 * The maximum number of children allowed in a node.
	 */
	protected int maxChildren;
	
	/**
	 * Indicates whether a branch split has occurred.
	 */
	protected boolean branchSplit;
	
	/**
	 * The name of the R-tree.
	 */
	protected String treeName;
	
	/**
	 * Logger for logging operations.
	 */
	protected ILogger logger;
	
	/**
	 * Default constructor for SplitBehaviorBase.
	 */
	public SplitBehaviorBase() {
		this.cache = null;
		this.maxChildren = 0;
		this.branchSplit = false;
		this.treeName = null;
		this.logger = null;
	}
	
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
