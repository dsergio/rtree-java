package rtree.storage;

import java.util.List;

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
	
	public void close();
	
	public String getTreeName();
	
	public void initializeStorage();
	public IRTreeNode addCloudRTreeNode(String nodeId, String children, String parent, String items, String rectangle, String treeName, IRTreeCache cache);
	public void updateItem(String tableName, String nodeId, String children, String parent, String items, String rectangle);
	public IRTreeNode getCloudRTreeNode(String tableName, String nodeId, IRTreeCache cache);
	
	// use these for performance analysis
	public int getNumAdds();
	public int getNumReads();
	public int getNumUpdates();
	public long getAddTime();
	public long getReadTime();
	public long getUpdateTime();
	
	
	public void addToMetaData(String treeName, int maxChildren, int maxItems); // store the maxChildren and maxItems values
	public void addToMetaDataNDimensional(String treeName, int maxChildren, int maxItems, int N); // store N, maxChildren and maxItems values
	public boolean metaDataExists(String treeName) throws Exception; // if treeName exists, use the metadata preferentially
	public int getMaxChildren(String treeName); // get the persistent maxChildren value
	public int getMaxItems(String treeName);  // get the persistent maxItems value
	public int getNumDimensions();  // get number of dimensions
	public void updateMetaDataBoundaries(int minX, int maxX, int minY, int maxY);
	public void updateMetaDataBoundariesNDimensional(List<Integer> minimums, List<Integer> maximums);
	public StorageType getStorageType();
	
	public void clearData();
	
}
