package cloudrtree;

/**
 * 
 * Storage Access Interface
 * 
 * @author David Sergio
 *
 */
public interface DBAccessRTree {
	
	public void init() throws Exception;
	public void close();
	
	public void createTable(String tableName) throws Exception;
	public CloudRTreeNode addCloudRTreeNode(String nodeId, String children, String parent, String items, String rectangle, String treeName, CloudRTreeCache cache);
	public void updateItem(String tableName, String nodeId, String children, String parent, String items, String rectangle);
	public CloudRTreeNode getCloudRTreeNode(String tableName, String nodeId, CloudRTreeCache cache);
	
	public int getNumAdds();
	public int getNumReads();
	public int getNumUpdates();
	public long getAddTime();
	public long getReadTime();
	public long getUpdateTime();
	
	
	public void addToMetaData(String tableName, int maxChildren, int maxItems);
	public boolean metaDataExists(String treeName);
	public int getMaxChildren(String treeName);
	public int getMaxItems(String treeName);
	
}
