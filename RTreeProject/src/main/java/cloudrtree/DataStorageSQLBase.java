package cloudrtree;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
 * SQL Storage Base Class
 * 
 * @author David Sergio
 *
 */
public abstract class DataStorageSQLBase extends DataStorageBase {

	protected Connection conn;
	
	protected DataStorageSQLBase(StorageType storageType, ILogger logger, String treeName) {
		super(storageType, logger, treeName);
		init();
	}

	@Override
	public abstract void init();

	@Override
	public void close() {

		try {
			conn.close();
		} catch (SQLException e) {
			logger.log(e);
			e.printStackTrace();
		}

	}

	@Override
	public abstract void initializeStorage();

	@Override
	public RTreeNode addCloudRTreeNode(String nodeId, String children, String parent, String items, String rectangle,
			String treeName, RTreeCache cache) {

		long time = System.currentTimeMillis();
//		logger.log("Adding nodeId: " + nodeId + ", children: " + children + ", parent: " + parent + ", items: " + items + ", rectangle: " + rectangle);

		String query = "INSERT INTO `rtree_data` (`nodeId`, `parent`, `rectangle`, `items`, `children`) "
				+ "VALUES (?, ?, ?, ?, ?);";

		PreparedStatement stmt = null;
		int c = 1;

		try {

			stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

			stmt.setString(c++, nodeId);
			stmt.setString(c++, parent);
			stmt.setString(c++, rectangle);
			stmt.setString(c++, items);
			stmt.setString(c++, children);

			logger.log("[QUERY]: " + stmt.toString());

			stmt.executeUpdate();

		} catch (SQLException e) {
			logger.log(e);
			e.printStackTrace();
		}
		
		RTreeNode node = new RTreeNode(nodeId, children, parent, cache, logger);
		Rectangle r = new Rectangle(rectangle);
		node.rectangle = r;
		node.setItemsJson(items);

		numAdds++;
		addTime += (System.currentTimeMillis() - time);

		return node;

	}

	@Override
	public void updateItem(String tableName, String nodeId, String children, String parent, String items,
			String rectangle) {

		long time = System.currentTimeMillis();
		
		String update = "UPDATE `rtree_data` ";

		String set = " SET nodeId = nodeId ";

		if (children != null) {
			set += ", `children` = ? ";
		}
		if (parent != null) {
			set += ", `parent` = ? ";
		}
		if (items != null) {
			set += ", `items` = ? ";
		}
		if (rectangle != null) {
			set += ", `rectangle` = ? ";
		}

		String where = " WHERE `nodeId` = ?";

		String query = update + set + where;

		int c = 1;

		try {

			PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

			if (children != null) {
				stmt.setString(c++, children);
			}
			if (parent != null) {
				stmt.setString(c++, parent);
			}
			if (items != null) {
				stmt.setString(c++, items);
			}
			if (rectangle != null) {
				stmt.setString(c++, rectangle);
			}
			stmt.setString(c++, nodeId);

			logger.log("[QUERY]: " + stmt.toString());

			stmt.executeUpdate();

		} catch (SQLException e) {
			logger.log(e);
			e.printStackTrace();
		}

		numUpdates++;
		updateTime += (System.currentTimeMillis() - time);
	}

	@Override
	public RTreeNode getCloudRTreeNode(String tableName, String nodeId, RTreeCache cache) {

		long time = System.currentTimeMillis();
		
		String select = " SELECT * FROM rtree_data ";

		String where = " WHERE `nodeId` = ?";

		String query = select + where;

		RTreeNode returnNode = null;

		PreparedStatement stmt;
		try {
			stmt = conn.prepareStatement(query);

			stmt.setString(1, nodeId);

			logger.log("[QUERY]: " + stmt.toString());

			ResultSet resultSet = stmt.executeQuery();

			String children = null;
			String parent = null;
			String rectangle = null;
			String items = null;

			Rectangle r = new Rectangle();
			if (resultSet.next()) {
				children = resultSet.getString("children");
				parent = resultSet.getString("parent");
				rectangle = resultSet.getString("rectangle");
				items = resultSet.getString("items");

				r = new Rectangle(rectangle);

				returnNode = new RTreeNode(nodeId, children, parent, cache, logger);
				returnNode.setRectangle(r);
				returnNode.setItemsJson(items);

				logger.log("select: nodeId: " + nodeId);
				logger.log("select: node children: " + returnNode.children);
				logger.log("select: node rectangle: " + r.toString());
				logger.log("select: node items: " + returnNode.getItemsJSON().toJSONString());
				logger.log("select: items: " + items);
				logger.log("select: parent: " + returnNode.parent);
			} else {

				logger.log("select: nodeId: " + nodeId);
				logger.log("select: node children: ");
				logger.log("select: node rectangle: ");
				logger.log("select: node items: ");
				logger.log("select: items: ");
				logger.log("select: parent: ");
			}

			return returnNode;

		} catch (SQLException e) {
			logger.log(e);
			e.printStackTrace();
		}

		numReads++;
		readTime += (System.currentTimeMillis() - time);

		return returnNode;
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
	public void addToMetaData(String treeName, int maxChildren, int maxItems) {
		String query = "INSERT INTO `rtree_metadata` (`treeName`, `maxChildren`, `maxItems`) " + "VALUES (?, ?, ?);";

		PreparedStatement stmt = null;
		int c = 1;

		try {

			stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(c++, treeName);
			stmt.setInt(c++, maxChildren);
			stmt.setInt(c++, maxItems);

			stmt.executeUpdate();
		} catch (SQLException e) {
			logger.log(e);
			e.printStackTrace();
		}

	}

	@Override
	public boolean metaDataExists(String treeName) {

		String select = " SELECT * FROM `rtree_metadata` ";
		String where = " WHERE `treeName` = ? ";
		String query = select + where;

		PreparedStatement stmt;
		try {
			stmt = conn.prepareStatement(query);
			stmt.setString(1, treeName);

			ResultSet resultSet = stmt.executeQuery();

			if (resultSet.next()) {
				return true;
			}
		} catch (SQLException e) {
			logger.log(e);
			e.printStackTrace();
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
			stmt = conn.prepareStatement(query);

			stmt.setString(1, treeName);

			ResultSet resultSet = stmt.executeQuery();

			if (resultSet.next()) {
				maxChildren = resultSet.getInt("maxChildren");
			}

		} catch (SQLException e) {
			logger.log(e);
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
			stmt = conn.prepareStatement(query);

			stmt.setString(1, treeName);

			ResultSet resultSet = stmt.executeQuery();

			if (resultSet.next()) {
				maxItems = resultSet.getInt("maxItems");
			}

		} catch (SQLException e) {
			logger.log(e);
			e.printStackTrace();
		}

		return maxItems;
	}

	@Override
	public void updateMetaDataBoundaries(int minX, int maxX, int minY, int maxY) {
		
		String update = "UPDATE `rtree_metadata` ";
		String set = " SET treeName = treeName" + ", minX = ? " + ", maxX = ? " + ", minY = ? " + ", maxY = ? " + "";
		String where = " WHERE treeName = ? ";

		String updateQuery = update + set + where;

		PreparedStatement stmt = null;
		int c = 1;

		try {
			stmt = conn.prepareStatement(updateQuery, Statement.RETURN_GENERATED_KEYS);

			stmt.setInt(c++, minX);
			stmt.setInt(c++, maxX);
			stmt.setInt(c++, minY);
			stmt.setInt(c++, maxY);
			stmt.setString(c++, treeName);

			stmt.executeUpdate();

		} catch (SQLException e) {
			logger.log(e);
			e.printStackTrace();
		}

	}

}
