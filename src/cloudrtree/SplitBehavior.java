package cloudrtree;

public abstract class SplitBehavior {
	
	protected CloudRTreeCache cache;
	protected int maxChildren;
	protected boolean branchSplit;
	protected CloudRTreeNode root;
	
	public boolean didBranchSplit() {
		return branchSplit;
	}
	
//	public abstract void pointSeeds ??
	
	public abstract void splitLeafNode(CloudRTreeNode node, LocationItem locationItem);
	public abstract void splitBranchNode(CloudRTreeNode node);
	

}
