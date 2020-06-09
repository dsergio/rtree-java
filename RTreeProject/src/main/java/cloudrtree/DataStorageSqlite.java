package cloudrtree;

/**
 * 
 * Example of another RTree storage implementation
 * 
 * @author David Sergio
 *
 */
public class DataStorageSqlite extends DataStorageBase {

	public DataStorageSqlite(ILogger logger, String treeName) {
		super(StorageType.SQLITE, logger, treeName);
	}

	@Override
	public void init() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initializeStorage() {
		// TODO Auto-generated method stub
	}

	@Override
	public RTreeNode addCloudRTreeNode(String nodeId, String children, String parent, String items, String rectangle,
			String treeName, RTreeCache cache) {
		return null;

	}

	@Override
	public void updateItem(String tableName, String nodeId, String children, String parent, String items,
			String rectangle) {
		// TODO Auto-generated method stub

	}

	@Override
	public RTreeNode getCloudRTreeNode(String tableName, String nodeId, RTreeCache cache) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getNumAdds() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumReads() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumUpdates() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getAddTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getReadTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getUpdateTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void addToMetaData(String tableName, int maxChildren, int maxItems) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean metaDataExists(String treeName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getMaxChildren(String treeName) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMaxItems(String treeName) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void updateMetaDataBoundaries(int minX, int maxX, int minY, int maxY) {
		// TODO Auto-generated method stub
		
	}

}
