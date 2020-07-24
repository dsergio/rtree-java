package rtree.storage;

import java.util.List;

import rtree.item.ILocationItem;
import rtree.tree.IRTree;
import rtree.tree.IRTreeCache;
import rtree.tree.IRTreeNode;

/**
 * 
 * Storage Access Interface
 * 
 * @author David Sergio
 *
 */
public interface IDataStorage {
	
	 void close();
	
	 List<IRTree> getAllTrees();
	 List<ILocationItem> getAllLocationItems();
	
	 void initializeStorage();
	 IRTreeNode addCloudRTreeNode(String nodeId, String children, String parent, String items, String rectangle, String treeName, IRTreeCache cache);
	 void updateItem(String treeName, String nodeId, String children, String parent, String items, String rectangle);
	 IRTreeNode getCloudRTreeNode(String treeName, String nodeId, IRTreeCache cache);
	
	 void addItem(String Id, int N, String location, String type);
	
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
	 void updateMetaDataBoundariesNDimensional(List<Integer> minimums, List<Integer> maximums, String treeName);
	 StorageType getStorageType();
	
	 void clearData();
	
}
