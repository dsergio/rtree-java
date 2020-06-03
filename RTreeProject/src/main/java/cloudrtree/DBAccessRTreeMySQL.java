package cloudrtree;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
 * Implementation of persistent storage in MySQL
 * 
 * @author David Sergio
 *
 */
public class DBAccessRTreeMySQL implements DBAccessRTree {
	
	private MysqlConnection connection;
	private String tableName = null;
	
	private int numReads = 0;
	private int numAdds = 0;
	private int numUpdates = 0;
	private long readTime = 0;
	private long addTime = 0;
	private long updateTime = 0;
	
	public DBAccessRTreeMySQL() throws Exception {
		init();
	}

	@Override
	public void init() throws Exception {
		
		String creds = "../../creds.txt";
		connection = new MysqlConnection(creds);

	}

	@Override
	public void close() {

		connection.close();

	}

	@Override
	public void initializeStorage(String tableName) throws Exception {
		
		connection.initializeDb(tableName);
		this.tableName = tableName;
	}

	@Override
	public CloudRTreeNode addCloudRTreeNode(String nodeId, String children, String parent, String items, String rectangle,
			String treeName, CloudRTreeCache cache) {
		
		long time = System.currentTimeMillis();
		System.out.println("Adding nodeId: " + nodeId + ", children: " + children + ", parent: " + parent + ", items: " + items + ", rectangle: " + rectangle);
		
		
		
		boolean success = connection.insert(nodeId, children, parent, items, rectangle, tableName);
		
		if (success) {
			CloudRTreeNode node = new CloudRTreeNode(nodeId, children, parent, cache);
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
		connection.update(tableName, nodeId, children, parent, items, rectangle);
		
		numUpdates++;
		updateTime += (System.currentTimeMillis() - time);
	}

	@Override
	public CloudRTreeNode getCloudRTreeNode(String tableName, String nodeId, CloudRTreeCache cache) {
		
		long time = System.currentTimeMillis();
		CloudRTreeNode node = connection.select(tableName, nodeId, cache);
		
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
		return tableName;
	}

}
