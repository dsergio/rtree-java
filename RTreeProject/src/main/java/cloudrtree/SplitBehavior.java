package cloudrtree;

/**
 * 
 * Split Behavior base class
 * 
 * @author David Sergio
 *
 */
public abstract class SplitBehavior {
	
	protected RTreeCache cache;
	protected int maxChildren;
	protected boolean branchSplit;
//	protected RTreeNode root;
	protected String treeName;
	protected ILogger logger;
	
	public boolean didBranchSplit() {
		return branchSplit;
	}
	
	public RTreeNode getNode(String nodeId) {
		return cache.getNode(nodeId);
	}
	
	public abstract void splitLeafNode(RTreeNode node, ILocationItem locationItem);
	public abstract void splitBranchNode(RTreeNode node);
	public abstract String getDescription();
	
//	public void initialize(int maxChildren, String treeName, RTreeCache cacheContainer, RTreeNode root, ILogger logger) {
//		this.maxChildren = maxChildren;
//		this.treeName = treeName;
//		this.cache = cacheContainer;
//		this.root = root;
//		this.logger = logger;
//	}
	public void initialize(int maxChildren, String treeName, RTreeCache cacheContainer, ILogger logger) {
		this.maxChildren = maxChildren;
		this.treeName = treeName;
		this.cache = cacheContainer;
		this.logger = logger;
	}

}
