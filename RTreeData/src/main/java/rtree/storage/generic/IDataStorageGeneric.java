package rtree.storage.generic;

import java.util.List;

import rtree.item.ILocationItem;
import rtree.item.generic.ILocationItemGeneric;
import rtree.item.generic.IRType;
import rtree.storage.StorageType;
import rtree.tree.IRTree;
import rtree.tree.IRTreeCache;
import rtree.tree.IRTreeNode;
import rtree.tree.generic.IRTreeCacheGeneric;
import rtree.tree.generic.IRTreeGeneric;
import rtree.tree.generic.IRTreeNodeGeneric;

/**
 * 
 * Storage Access Interface
 * 
 * @author David Sergio
 *
 */
public interface IDataStorageGeneric<T extends IRType<T>> {
	
	 void close();
	
	 List<IRTreeGeneric<T>> getAllTrees();
	 List<ILocationItemGeneric<T>> getAllLocationItems();
	
	 void initializeStorage();
	 IRTreeNodeGeneric<T> addCloudRTreeNode(String nodeId, String children, String parent, String items, String rectangle, String treeName, IRTreeCacheGeneric<T> cache);
	 void updateItem(String treeName, String nodeId, String children, String parent, String items, String rectangle);
	 IRTreeNodeGeneric<T> getCloudRTreeNode(String treeName, String nodeId, IRTreeCacheGeneric<T> cache);
	
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
	
}
