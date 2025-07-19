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
	
	 void close();
	
	 List<IRTree<T>> getAllTrees();
	 List<ILocationItem<T>> getAllLocationItems();
	
	 void initializeStorage();
	 IRTreeNode<T> addCloudRTreeNode(String nodeId, String children, String parent, String items, String rectangle, String treeName, IRTreeCache<T> cache);
	 void updateItem(String treeName, String nodeId, String children, String parent, String items, String rectangle);
	 IRTreeNode<T> getCloudRTreeNode(String treeName, String nodeId, IRTreeCache<T> cache);
	
	 void addItem(String Id, int N, String location, String type, String properties);
	
	// use these for performance analysis
	 int getNumAdds();
	 int getNumReads();
	 int getNumUpdates();
	 long getAddTime();
	 long getReadTime();
	 long getUpdateTime();
	
	
	 void addToMetaData(String treeName, int maxChildren, int maxItems); // store the maxChildren and maxItems values
	 void addToMetaDataNDimensional(String treeName, int maxChildren, int maxItems, int N); // store N, maxChildren and maxItems values
	 boolean metaDataExists(String treeName) throws Exception; // if treeName exists, use the metadata preferentially
	 int getMaxChildren(String treeName); // get the persistent maxChildren value
	 int getMaxItems(String treeName);  // get the persistent maxItems value
	 int getNumDimensions(String treeName);  // get number of dimensions
	 void updateMetaDataBoundaries(int minX, int maxX, int minY, int maxY, String treeName);
	 void updateMetaDataBoundariesNDimensional(List<T> minimums, List<T> maximums, String treeName);
	 StorageType getStorageType();
	
	 void clearData();
	 
	 List<T> getMin(String treeName);
	 List<T> getMax(String treeName);
	 
	 boolean isDbConnected();
	
}
