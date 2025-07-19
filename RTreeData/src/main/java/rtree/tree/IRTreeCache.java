package rtree.tree;

import rtree.item.IRType;
import rtree.storage.IDataStorage;

/**
 * 
 * @author David Sergio
 *
 */
public interface IRTreeCache<T extends IRType<T>> {
	
	 int getNumDimensions();
	 void printCache();
	 IDataStorage<T> getDBAccess();
	 IRTreeNode<T> getNode(String nodeId);
	 void updateNode(String nodeId, String children, String parent, String items, String rectangle);
	 void addNode(String nodeId, String children, String parent, String items, String rectangle, IRTreeNode<T> node);
	 void addNode(String nodeId, String children, String parent, String items, String rectangle);
	 void remove(String node);
	
}
