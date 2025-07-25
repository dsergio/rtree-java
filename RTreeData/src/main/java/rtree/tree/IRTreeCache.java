package rtree.tree;

import rtree.item.IRType;
import rtree.storage.IDataStorage;

/**
 * 
 * @author David Sergio
 *
 */
public interface IRTreeCache<T extends IRType<T>> {
	
	 void printCache();
	 
	 IDataStorage<T> getDBAccess();
	 IRTree<T> getTree();
	 
	 IRTreeNode<T> getNode(String nodeId);
	 void removeNode(String nodeId);
	 void putNode(String nodeId, IRTreeNode<T> node);
	 void updateNode(String nodeId, IRTreeNode<T> node);
	 
}
