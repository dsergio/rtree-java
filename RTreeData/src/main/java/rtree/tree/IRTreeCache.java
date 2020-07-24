package rtree.tree;

import rtree.storage.IDataStorage;

/**
 * 
 * @author David Sergio
 *
 */
public interface IRTreeCache {
	
	 int getNumDimensions();
	 void printCache();
	 IDataStorage getDBAccess();
	 IRTreeNode getNode(String nodeId);
	 void updateNode(String nodeId, String children, String parent, String items, String rectangle);
	 void addNode(String nodeId, String children, String parent, String items, String rectangle, IRTreeNode node);
	 void addNode(String nodeId, String children, String parent, String items, String rectangle);
	 void remove(String node);
	
}
