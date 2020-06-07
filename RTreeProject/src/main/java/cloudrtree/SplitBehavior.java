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
	protected RTreeNode root;
	protected String treeName;
	protected ILogger logger;
	
	public boolean didBranchSplit() {
		return branchSplit;
	}
	
	public abstract void splitLeafNode(RTreeNode node, LocationItem locationItem);
	public abstract void splitBranchNode(RTreeNode node);
	public abstract String getDescription();
	
	public void initialize(int maxChildren, String treeName, RTreeCache cacheContainer, RTreeNode root, ILogger logger) {
		this.maxChildren = maxChildren;
		this.treeName = treeName;
		this.cache = cacheContainer;
		this.root = root;
		this.logger = logger;
	}	

}
