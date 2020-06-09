package cloudrtree;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
 * MySQL Storage Implementation
 * 
 * @author David Sergio
 *
 */
public class DataStorageMySQL extends DataStorageBase {
	
	private DataStorageMysqlConnection connection;
	
	private int numReads = 0;
	private int numAdds = 0;
	private int numUpdates = 0;
	private long readTime = 0;
	private long addTime = 0;
	private long updateTime = 0;
	
	public DataStorageMySQL(ILogger logger, String treeName) throws Exception {
		super(StorageType.MYSQL, logger, treeName);
		init();
	}

	@Override
	public void init() throws Exception {
		
		String creds = "../../creds.txt";
		connection = new DataStorageMysqlConnection(creds, logger);

	}

	@Override
	public void close() {

		connection.close();

	}

	@Override
	public void initializeStorage() throws Exception {
		connection.initializeDb(treeName);
	}

	@Override
	public RTreeNode addCloudRTreeNode(String nodeId, String children, String parent, String items, String rectangle,
			String treeName, RTreeCache cache) {
		
		long time = System.currentTimeMillis();
//		logger.log("Adding nodeId: " + nodeId + ", children: " + children + ", parent: " + parent + ", items: " + items + ", rectangle: " + rectangle);
		
		
		
		boolean success = connection.insert(nodeId, children, parent, items, rectangle, this.treeName);
		
		if (success) {
			RTreeNode node = new RTreeNode(nodeId, children, parent, cache, logger);
			Rectangle r = new Rectangle(rectangle);
			node.rectangle = r;
			node.setItemsJson(items);
			
			numAdds++;
			addTime += (System.currentTimeMillis() - time);
			
			return node;
		} else {
			return null;
		}

	}

	@Override
	public void updateItem(String tableName, String nodeId, String children, String parent, String items,
			String rectangle) {
		
		long time = System.currentTimeMillis();
		connection.update(this.treeName, nodeId, children, parent, items, rectangle);
		
		numUpdates++;
		updateTime += (System.currentTimeMillis() - time);
	}

	@Override
	public RTreeNode getCloudRTreeNode(String tableName, String nodeId, RTreeCache cache) {
		
		long time = System.currentTimeMillis();
		RTreeNode node = connection.select(this.treeName, nodeId, cache);
		
		numReads++;
		readTime += (System.currentTimeMillis() - time);
		
		return node;
	}

	@Override
	public int getNumAdds() {
		return numAdds;
	}

	@Override
	public int getNumReads() {
		return numReads;
	}

	@Override
	public int getNumUpdates() {
		return numUpdates;
	}

	@Override
	public long getAddTime() {
		return addTime;
	}

	@Override
	public long getReadTime() {
		return readTime;
	}

	@Override
	public long getUpdateTime() {
		return updateTime;
	}

	@Override
	public void addToMetaData(String treeName, int maxChildren, int maxItems) throws SQLException {
		String query = "INSERT INTO `rtree_metadata` (`treeName`, `maxChildren`, `maxItems`) "
				+ "VALUES (?, ?, ?);";

		PreparedStatement stmt = null;
		int c = 1;


		stmt = connection.getConn().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

		stmt.setString(c++, treeName);
		stmt.setInt(c++, maxChildren);
		stmt.setInt(c++, maxItems);

		stmt.executeUpdate();



	}

	@Override
	public boolean metaDataExists(String treeName) throws Exception {
		
		String select = " SELECT * FROM `rtree_metadata` ";
		String where = " WHERE `treeName` = ? ";
		String query = select + where;
		
		PreparedStatement stmt = connection.getConn().prepareStatement(query);

		stmt.setString(1, treeName);
		
		ResultSet resultSet = stmt.executeQuery();
		
		if (resultSet.next()) {
			return true;
		}
		
		return false;
	}

	@Override
	public int getMaxChildren(String treeName) {
		
		int maxChildren = 4; // default to 4
		
		String select = " SELECT * FROM `rtree_metadata` ";
		String where = " WHERE `treeName` = ? ";
		String query = select + where;
		
		PreparedStatement stmt;
		try {
			stmt = connection.getConn().prepareStatement(query);
			
			stmt.setString(1, treeName);
			
			ResultSet resultSet = stmt.executeQuery();
			
			if (resultSet.next()) {
				maxChildren = resultSet.getInt("maxChildren");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return maxChildren;
	}

	@Override
	public int getMaxItems(String treeName) {
		int maxItems = 4; // default to 4
		
		String select = " SELECT * FROM `rtree_metadata` ";
		String where = " WHERE `treeName` = ? ";
		String query = select + where;
		
		PreparedStatement stmt;
		try {
			stmt = connection.getConn().prepareStatement(query);
			
			stmt.setString(1, treeName);
			
			ResultSet resultSet = stmt.executeQuery();
			
			if (resultSet.next()) {
				maxItems = resultSet.getInt("maxItems");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return maxItems;
	}

	public String getTableName() {
		return treeName;
	}

	@Override
	public void updateMetaDataBoundaries(int minX, int maxX, int minY, int maxY) {
		String update = "UPDATE `rtree_metadata` ";
		String set = " SET treeName = treeName"
				+ ", minX = ? "
				+ ", maxX = ? "
				+ ", minY = ? "
				+ ", maxY = ? "
				+ "";
		String where = " WHERE treeName = ? ";
		
		String updateQuery = update + set + where;

		PreparedStatement stmt = null;
		int c = 1;


		try {
			stmt = connection.getConn().prepareStatement(updateQuery, Statement.RETURN_GENERATED_KEYS);
			
			stmt.setInt(c++, minX);
			stmt.setInt(c++, maxX);
			stmt.setInt(c++, minY);
			stmt.setInt(c++, maxY);
			stmt.setString(c++, treeName);

			stmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
	}

}
