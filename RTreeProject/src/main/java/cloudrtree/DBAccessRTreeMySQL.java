package cloudrtree;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 * 
 * Description TBD
 * 
 * @author David Sergio
 *
 */
public class DBAccessRTreeMySQL implements DBAccessRTree {
	
	private MysqlConnection connection;
	private String tableName = null;
	
	public DBAccessRTreeMySQL() {
		try {
			init();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	public void createTable(String tableName) {
		
		connection.createTable(tableName);
		this.tableName = tableName;
	}

	@Override
	public CloudRTreeNode addCloudRTreeNode(String nodeId, String children, String parent, String items, String rectangle,
			String treeName, CloudRTreeCache cache) {
		
		
		System.out.println("Adding nodeId: " + nodeId + ", children: " + children + ", parent: " + parent + ", items: " + items + ", rectangle: + " + rectangle);
		
		
		
		boolean success = connection.insert(nodeId, children, parent, items, rectangle, tableName);
		
		if (success) {
			CloudRTreeNode node = new CloudRTreeNode(nodeId, children, parent, cache);
			return node;
		} else {
			return null;
		}

	}

	@Override
	public void updateItem(String tableName, String nodeId, String children, String parent, String items,
			String rectangle) {
		
		connection.update(tableName, nodeId, children, parent, items, rectangle);

	}

	@Override
	public CloudRTreeNode getCloudRTreeNode(String tableName, String nodeId, CloudRTreeCache cache) {
		
		CloudRTreeNode node = connection.select(tableName, nodeId, cache);
		
		return node;
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

	public String getTableName() {
		return tableName;
	}

}
