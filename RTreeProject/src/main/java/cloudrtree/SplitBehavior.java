package cloudrtree;

/**
 * 
 * Description TBD
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
	

}
