package rtree.tree;

import rtree.storage.DepIDataStorage;

public interface DepIRTreeCache {
	
	public int getNumDimensions();
	public void printCache();
	public DepIDataStorage getDBAccess();
	public DepRTreeNode getNode(String nodeId);
	public void updateNode2D(String nodeId, String children, String parent, String items, String rectangle);
	public void updateNodeNDimensional(String nodeId, String children, String parent, String items, String rectangle);
	public void addNode(String nodeId, String children, String parent, String items, String rectangle, DepRTreeNode node);
	public void addNode(String nodeId, String children, String parent, String items, String rectangle);
	public void remove(String node);
	
}
