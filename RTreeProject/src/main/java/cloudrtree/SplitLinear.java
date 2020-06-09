package cloudrtree;

/**
 * 
 * Description TBD
 * 
 * @author David Sergio
 *
 */
public class SplitLinear extends SplitBehavior {

	final String description;
	
	public SplitLinear() {
		description = "Linear Split";
	}
	
	@Override
	public String getDescription() {
		return description;
	}
	@Override
	public void splitLeafNode(RTreeNode node, ILocationItem locationItem) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void splitBranchNode(RTreeNode node) {
		// TODO Auto-generated method stub
		
	}

}
