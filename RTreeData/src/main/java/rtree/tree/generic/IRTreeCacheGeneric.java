package rtree.tree.generic;

import rtree.item.generic.IRType;
import rtree.storage.IDataStorage;
import rtree.storage.generic.IDataStorageGeneric;

/**
 * 
 * @author David Sergio
 *
 */
public interface IRTreeCacheGeneric<T extends IRType<T>> {
	
	 int getNumDimensions();
	 void printCache();
	 IDataStorageGeneric<T> getDBAccess();
	 IRTreeNodeGeneric<T> getNode(String nodeId);
	 void updateNode(String nodeId, String children, String parent, String items, String rectangle);
	 void addNode(String nodeId, String children, String parent, String items, String rectangle, IRTreeNodeGeneric<T> node);
	 void addNode(String nodeId, String children, String parent, String items, String rectangle);
	 void remove(String node);
	
}
