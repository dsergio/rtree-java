package co.dsergio.rtree.business.dto;


public class RTreeCreate {
	
	public String treeName;
	public int numDimensions;
	public int maxChildren;
	public int maxItems;
	
	public RTreeCreate() {
	}
	
	public RTreeCreate(String treeName, int numDimensions, int maxChildren, int maxItems) {
		this.treeName = treeName;
		this.numDimensions = numDimensions;
		this.maxChildren = maxChildren;
		this.maxItems = maxItems;
	}
	
	
}
