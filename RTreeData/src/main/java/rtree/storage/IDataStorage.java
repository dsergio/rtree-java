package rtree.storage;

import java.util.List;

import rtree.item.ILocationItem;
import rtree.item.IRType;
import rtree.tree.IRTreeCache;
import rtree.tree.IRTree;
import rtree.tree.IRTreeNode;

/**
 * 
 * Storage Access Interface
 * 
 * @author David Sergio
 *
 */
public interface IDataStorage<T extends IRType<T>> {
	
	 List<IRTree<T>> getAllTrees();
	 List<ILocationItem<T>> getAllLocationItems();
	 List<T> getMin(String treeName);
	 List<T> getMax(String treeName);
	
	 
	 IRTreeNode<T> addRTreeNode(String nodeId, String children, String parent, String items, String rectangle, String treeName, IRTreeCache<T> cache);
	 IRTreeNode<T> getRTreeNode(String treeName, String nodeId, IRTreeCache<T> cache);
	 void updateRTreeNode(String treeName, String nodeId, String children, String parent, String items, String rectangle);
	 void addLocationItem(String Id, int N, String location, String type, String properties);
	 
	
	 // use these for performance analysis
	 int getNumAdds();
	 int getNumReads();
	 int getNumUpdates();
	 long getAddTime();
	 long getReadTime();
	 long getUpdateTime();
	
	
	 // metadata methods
	 void addToMetaData(String treeName, int maxChildren, int maxItems, int N); // store N, maxChildren and maxItems values
	 boolean metaDataExists(String treeName) throws Exception; // if treeName exists, use the metadata preferentially
	 int getMaxChildren(String treeName); // get the persistent maxChildren value
	 int getMaxItems(String treeName);  // get the persistent maxItems value
	 int getNumDimensions(String treeName);  // get number of dimensions
	 void updateMetaDataBoundaries(List<T> minimums, List<T> maximums, String treeName);
	 
	 
	 // storage management methods
	 void initializeStorage();
	 void close();
	 StorageType getStorageType();
	 void clearData();
	 boolean isDbConnected();
}

