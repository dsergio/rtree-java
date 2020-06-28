package rtree.storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import rtree.log.ILogger;
import rtree.rectangle.RectangleND;
import rtree.rectangle.IHyperRectangle;
import rtree.rectangle.Rectangle2D;
import rtree.tree.IRTreeCache;
import rtree.tree.IRTreeNode;
import rtree.tree.RTreeNode2D;
import rtree.tree.RTreeNodeND;

/**
 * 
 * SQL Storage Base Class
 * 
 * @author David Sergio
 *
 */
public abstract class DataStorageSQLBase extends DataStorageBase {

	protected Connection conn;
	
	protected DataStorageSQLBase(StorageType storageType, ILogger logger, String treeName, int numDimensions) {
		super(storageType, logger, treeName, numDimensions);
	}

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
	public IRTreeNode addCloudRTreeNode(String nodeId, String children, String parent, String items, String rectangle,
			String treeName, IRTreeCache cache) {

		long time = System.currentTimeMillis();
		logger.log("Adding nodeId: " + nodeId + ", children: " + children + ", parent: " + parent + ", items: " + items + ", rectangle: " + rectangle);

		String query = "INSERT INTO `" + tablePrefix + "_data` (`nodeId`, `parent`, `rectangle`, `items`, `children`) "
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
		
		IRTreeNode node = null;
		if (numDimensions == 2) {
			node = new RTreeNode2D(nodeId, children, parent, cache, logger);
		} else {
			node = new RTreeNodeND(nodeId, children, parent, cache, logger);
		}
		
		IHyperRectangle r;
		if (numDimensions == 2) {
			r = new Rectangle2D();
		} else {
			r = new RectangleND(numDimensions);
		}
		
		if (rectangle != null) {
			JSONParser parser = new JSONParser();
			JSONObject rObj;
			try {
				rObj = (JSONObject) parser.parse(rectangle);
				for (int i = 0; i < numDimensions; i++) {
					switch (i) {
					case 0: 
						r.setDim1(i, Integer.parseInt(rObj.get("x1").toString()));
						r.setDim2(i, Integer.parseInt(rObj.get("x2").toString()));
						break;
					case 1:
						r.setDim1(i, Integer.parseInt(rObj.get("y1").toString()));
						r.setDim2(i, Integer.parseInt(rObj.get("y2").toString()));
						break;
					case 2:
						r.setDim1(i, Integer.parseInt(rObj.get("z1").toString()));
						r.setDim2(i, Integer.parseInt(rObj.get("z2").toString()));
						break;
					default:
						r.setDim1(i, Integer.parseInt(rObj.get(i + "_1").toString()));
						r.setDim2(i, Integer.parseInt(rObj.get(i + "_2").toString()));
						break;
					}
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
//		Rectangle r = new Rectangle(rectangle);
		
		
		
		node.setRectangle(r);
		node.setItemsJson(items);

		numAdds++;
		addTime += (System.currentTimeMillis() - time);

		return node;

	}

	@Override
	public void updateItem(String tableName, String nodeId, String children, String parent, String items,
			String rectangle) {

		long time = System.currentTimeMillis();
		
		String update = "UPDATE `" + tablePrefix + "_data` ";

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
	public IRTreeNode getCloudRTreeNode(String tableName, String nodeId, IRTreeCache cache) {

		long time = System.currentTimeMillis();
		
		String select = " SELECT * FROM " + tablePrefix + "_data ";

		String where = " WHERE `nodeId` = ?";

		String query = select + where;

		IRTreeNode returnNode = null;

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
			
			int N = cache.getNumDimensions();
			
			IHyperRectangle r;
			if (N == 2) {
				r = new Rectangle2D();
			} else {
				r = new RectangleND(N);
			}
			
			if (resultSet.next()) {
				children = resultSet.getString("children");
				parent = resultSet.getString("parent");
				rectangle = resultSet.getString("rectangle");
				items = resultSet.getString("items");

//				r = new Rectangle(rectangle);
				
				if (rectangle != null) {
					JSONParser parser = new JSONParser();
					JSONObject rObj;
					try {
						rObj = (JSONObject) parser.parse(rectangle);
						
						for (int i = 0; i < N; i++) {
							switch (i) {
							case 0: 
								r.setDim1(i, Integer.parseInt(rObj.get("x1").toString()));
								r.setDim2(i, Integer.parseInt(rObj.get("x2").toString()));
								break;
							case 1:
								r.setDim1(i, Integer.parseInt(rObj.get("y1").toString()));
								r.setDim2(i, Integer.parseInt(rObj.get("y2").toString()));
								break;
							case 2:
								r.setDim1(i, Integer.parseInt(rObj.get("z1").toString()));
								r.setDim2(i, Integer.parseInt(rObj.get("z2").toString()));
								break;
							default:
								r.setDim1(i, Integer.parseInt(rObj.get(i + "_1").toString()));
								r.setDim2(i, Integer.parseInt(rObj.get(i + "_2").toString()));
								break;
							}
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				
				if (cache.getNumDimensions() == 2) {
					returnNode = new RTreeNode2D(nodeId, children, parent, cache, logger);
				} else {
					returnNode = new RTreeNodeND(nodeId, children, parent, cache, logger);
				}

				returnNode.setRectangle(r);
				returnNode.setItemsJson(items);

				logger.log("select: nodeId: " + nodeId);
				logger.log("select: node children: " + returnNode.getChildren());
				logger.log("select: node rectangle: " + r.toString());
				logger.log("select: node items: " + returnNode.getItemsJSON().toJSONString());
				logger.log("select: items: " + items);
				logger.log("select: parent: " + returnNode.getParent());
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
		String query = "INSERT INTO `" + tablePrefix + "_metadata` (`treeName`, `maxChildren`, `maxItems`) " + "VALUES (?, ?, ?);";

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
	public void addToMetaDataNDimensional(String treeName, int maxChildren, int maxItems, int N) {
		String query = "INSERT INTO `" + tablePrefix + "_metadata` (`treeName`, `maxChildren`, `maxItems`, `N`) " + "VALUES (?, ?, ?, ?);";

		PreparedStatement stmt = null;
		int c = 1;

		try {

			stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(c++, treeName);
			stmt.setInt(c++, maxChildren);
			stmt.setInt(c++, maxItems);
			stmt.setInt(c++, N);

			stmt.executeUpdate();
		} catch (SQLException e) {
			logger.log(e);
			e.printStackTrace();
		}

	}

	@Override
	public boolean metaDataExists(String treeName) {

		String select = " SELECT * FROM `" + tablePrefix + "_metadata` ";
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

		String select = " SELECT * FROM `" + tablePrefix + "_metadata` ";
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

		String select = " SELECT * FROM `" + tablePrefix + "_metadata` ";
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
	public int getNumDimensions() {
		
		int N = 2; // default to 2
		
		String select = " SELECT * FROM `" + tablePrefix + "_metadata` ";
		String where = " WHERE `treeName` = ? ";
		String query = select + where;

		PreparedStatement stmt;
		try {
			stmt = conn.prepareStatement(query);

			stmt.setString(1, treeName);

			ResultSet resultSet = stmt.executeQuery();

			if (resultSet.next()) {
				N = resultSet.getInt("N");
			}

		} catch (SQLException e) {
			logger.log(e);
			e.printStackTrace();
		}

		return N;
	}

	@Override
	public void updateMetaDataBoundaries(int minX, int maxX, int minY, int maxY) {
		
		String update = "UPDATE `" + tablePrefix + "_metadata` ";
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
	
	@SuppressWarnings("unchecked")
	@Override
	public void updateMetaDataBoundariesNDimensional(List<Integer> minimums, List<Integer> maximums) {
		
		JSONArray arrMin = new JSONArray();
		JSONArray arrMax = new JSONArray();
		for (int i = 0; i < minimums.size(); i++) {
			arrMin.add(minimums.get(i));
			arrMax.add(maximums.get(i));
		}
		
		String update = "UPDATE `" + tablePrefix + "_metadata` ";
		String set = " SET treeName = treeName" + ", minimums = ? " + ", maximums = ? ";
		String where = " WHERE treeName = ? ";

		String updateQuery = update + set + where;

		PreparedStatement stmt = null;
		int c = 1;

		try {
			stmt = conn.prepareStatement(updateQuery, Statement.RETURN_GENERATED_KEYS);

			stmt.setString(c++, arrMin.toJSONString());
			stmt.setString(c++, arrMax.toJSONString());
			stmt.setString(c++, treeName);

			stmt.executeUpdate();

		} catch (SQLException e) {
			logger.log(e);
			e.printStackTrace();
		}
		
	}

	@Override
	public void clearData() {
		
		
		String deleteData = "DELETE FROM " + tablePrefix + "_data WHERE 1; ";
		

		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement(deleteData, Statement.RETURN_GENERATED_KEYS);
			stmt.executeUpdate();

		} catch (SQLException e) {
			logger.log(e);
			e.printStackTrace();
		}
		
		String deleteMetadata = "DELETE FROM " + tablePrefix + "_metadata WHERE 1; ";
		
		try {
			stmt = conn.prepareStatement(deleteMetadata, Statement.RETURN_GENERATED_KEYS);
			stmt.executeUpdate();

		} catch (SQLException e) {
			logger.log(e);
			e.printStackTrace();
		}
		
	}
	
	

}
