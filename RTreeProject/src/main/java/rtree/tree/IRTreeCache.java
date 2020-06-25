package rtree.tree;

import rtree.storage.IDataStorage;

public interface IRTreeCache {
	
	public int getNumDimensions();
	public void printCache();
	public IDataStorage getDBAccess();
	public IRTreeNode getNode(String nodeId);
	
	
//	public void updateNode2D(String nodeId, String children, String parent, String items, String rectangle);
//	public void updateNodeNDimensional(String nodeId, String children, String parent, String items, String rectangle);
	
	public void updateNode(String nodeId, String children, String parent, String items, String rectangle);
	
	
	public void addNode(String nodeId, String children, String parent, String items, String rectangle, IRTreeNode node);
	public void addNode(String nodeId, String children, String parent, String items, String rectangle);
	public void remove(String node);
	
}
