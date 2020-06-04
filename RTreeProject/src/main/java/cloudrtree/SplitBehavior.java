package cloudrtree;

/**
 * 
 * Description TBD
 * 
 * @author David Sergio
 *
 */
public abstract class SplitBehavior {
	
	protected CloudRTreeCache cache;
	protected int maxChildren;
	protected boolean branchSplit;
	protected CloudRTreeNode root;
	protected String treeName;
	protected ILogger logger;
	
	public boolean didBranchSplit() {
		return branchSplit;
	}
	
	
	public abstract void splitLeafNode(CloudRTreeNode node, LocationItem locationItem);
	public abstract void splitBranchNode(CloudRTreeNode node);
	

}
