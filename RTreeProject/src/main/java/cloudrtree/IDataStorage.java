package cloudrtree;

import java.sql.SQLException;

/**
 * 
 * Storage Access Interface
 * 
 * @author David Sergio
 *
 */
public interface IDataStorage {
	
	public void init() throws Exception;
	public void close();
	
	public void initializeStorage(String treeName) throws Exception;
	public RTreeNode addCloudRTreeNode(String nodeId, String children, String parent, String items, String rectangle, String treeName, RTreeCache cache);
	public void updateItem(String tableName, String nodeId, String children, String parent, String items, String rectangle);
	public RTreeNode getCloudRTreeNode(String tableName, String nodeId, RTreeCache cache);
	
	// use these for performance analysis
	public int getNumAdds();
	public int getNumReads();
	public int getNumUpdates();
	public long getAddTime();
	public long getReadTime();
	public long getUpdateTime();
	
	
	public void addToMetaData(String treeName, int maxChildren, int maxItems) throws Exception; // store the maxChildren and maxItems values
	public boolean metaDataExists(String treeName) throws Exception; // if treeName exists, use the metadata preferentially
	public int getMaxChildren(String treeName); // get the persistent maxChildren value
	public int getMaxItems(String treeName);  // get the persistent maxItems value
	public void updateMetaDataBoundaries(int minX, int maxX, int minY, int maxY);
	
}
