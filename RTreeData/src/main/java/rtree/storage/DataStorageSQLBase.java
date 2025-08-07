package rtree.storage;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import rtree.item.ILocationItem;
import rtree.item.IRType;
import rtree.item.LocationItem;
import rtree.item.RDouble;
import rtree.log.ILogger;
import rtree.log.LogLevel;
import rtree.rectangle.IHyperRectangle;
import rtree.rectangle.HyperRectangle;
import rtree.tree.IRTreeCache;
import rtree.tree.IRTree;
import rtree.tree.IRTreeNode;
import rtree.tree.RTree;
import rtree.tree.RTreeNode;

/**
 * 
 * SQL Storage Base Class
 * 
 * @author David Sergio
 *
 */
public abstract class DataStorageSQLBase<T extends IRType<T>> extends DataStorageBase<T> {

	protected Connection conn;
	Class<T> className;
	
	public DataStorageSQLBase(StorageType storageType, ILogger logger, Class<T> className) {
		super(storageType, logger);
		this.className = className;
	}
	
	public T getInstanceOf() {
		try {
			return className.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		
		return null;
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
	public boolean isDbConnected() {
	    final String CHECK_SQL_QUERY = "SELECT 1";
	    boolean isConnected = false;
	    try {
	        conn.prepareStatement(CHECK_SQL_QUERY);
	        isConnected = true;
	    } catch (SQLException | NullPointerException e) {
	        return false;
	    }
	    return isConnected;
	}

	@Override
	public abstract void initializeStorage();

	@Override
	public int getNumDimensions(String treeName) {
		
		if (!isDbConnected()) {
			init();
		}
		
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
	public IRTreeNode<T> addRTreeNode(String nodeId, String children, String parent, String items, String rectangle,
			String treeName, IRTreeCache<T> cache) {

		if (!isDbConnected()) {
			init();
		}
		
		long time = System.currentTimeMillis();
		logger.log("[SQLBASE] Adding nodeId: " + nodeId + ", children: " + children + ", parent: " + parent + ", items: " + items + ", rectangle: " + rectangle);

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
		
		int numDimensions = getNumDimensions(treeName);
		
		IRTreeNode<T> node = null;
		node = new RTreeNode<T>(nodeId, children, parent, cache, logger, className);
		
		
		IHyperRectangle<T> r;
		r = new HyperRectangle<T>(numDimensions);
		
		
		if (rectangle != null) {
			JSONParser parser = new JSONParser();
			JSONObject rObj;
			try {
				rObj = (JSONObject) parser.parse(rectangle);
				for (int i = 0; i < numDimensions; i++) {
					
					T dim1 = getInstanceOf();
					T dim2 = getInstanceOf();
					r.setDim1(i, dim1);
					r.setDim2(i, dim2);
					
					switch (i) {
					case 0: 
						r.getDim1(i).setData(rObj.get("x1").toString());
						r.getDim2(i).setData(rObj.get("x2").toString());
						break;
					case 1:
						r.getDim1(i).setData(rObj.get("y1").toString());
						r.getDim2(i).setData(rObj.get("y2").toString());
						break;
					case 2:
						r.getDim1(i).setData(rObj.get("z1").toString());
						r.getDim2(i).setData(rObj.get("z2").toString());
						break;
					default:
						r.getDim1(i).setData(rObj.get(i + "_1").toString());
						r.getDim2(i).setData(rObj.get(i + "_2").toString());
						break;
					}
				}
			} catch (ParseException | IllegalArgumentException | SecurityException e) {
				e.printStackTrace();
			}
		}

		node.setRectangle(r);
		node.setLocationItemsJson(items);

//		numAdds++;
//		addTime += (System.currentTimeMillis() - time);
		performance.addAdd(System.currentTimeMillis() - time);

		return node;

	}
	
	

	@Override
	public void addLocationItem(String Id, int N, String location, String type, String properties) {
		String query = "INSERT INTO `" + tablePrefix + "_items` (`id`, `N`, `location`, `type`, `treeType`, `properties`) "
				+ "VALUES (?, ?, ?, ?, ?, ?);";

		
		if (!isDbConnected()) {
			init();
		}
		
		PreparedStatement stmt = null;
		int c = 1;

		try {

			stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

			stmt.setString(c++, Id);
			stmt.setInt(c++, N);
			stmt.setString(c++, location);
			stmt.setString(c++, type);
			stmt.setString(c++, className.getSimpleName());
			stmt.setString(c++, properties);

			logger.log("[QUERY] " + stmt.toString());

			stmt.executeUpdate();

		} catch (SQLException e) {
			logger.log(e);
			e.printStackTrace();
		}
		
	}

	@Override
	public void updateRTreeNode(String tableName, String nodeId, String children, String parent, String items,
			String rectangle) {

		
		if (!isDbConnected()) {
			init();
		}
		
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

			logger.log(stmt.toString(), "QUERY", LogLevel.DEBUG, true);

			stmt.executeUpdate();

		} catch (SQLException e) {
			logger.log(e);
			e.printStackTrace();
		}

//		numUpdates++;
//		updateTime += (System.currentTimeMillis() - time);
		performance.addUpdate(System.currentTimeMillis() - time);
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public IRTreeNode<T> getRTreeNode(String tableName, String nodeId, IRTreeCache<T> cache) {

		if (!isDbConnected()) {
			System.out.println("re-initializing db connection...");
			init();
		}
		
		long time = System.currentTimeMillis();
		
		String select = " SELECT * FROM " + tablePrefix + "_data ";

		String where = " WHERE `nodeId` = ?";

		String query = select + where;

		IRTreeNode<T> returnNode = null;

		PreparedStatement stmt;
		try {
			stmt = conn.prepareStatement(query);

			stmt.setString(1, nodeId);

			logger.log(stmt.toString(), "QUERY", LogLevel.DEBUG, true);

			ResultSet resultSet = stmt.executeQuery();

			String children = null;
			String parent = null;
			String rectangle = null;
			String items = null;
			
			int N = cache.getTree().getNumDimensions();
			
			IHyperRectangle<T> r = new HyperRectangle<T>(N);
			
			
			if (resultSet.next()) {
				children = resultSet.getString("children");
				parent = resultSet.getString("parent");
				rectangle = resultSet.getString("rectangle");
				items = resultSet.getString("items");

				
				if (rectangle != null) {
					JSONParser parser = new JSONParser();
					JSONObject rObj;
					try {
						rObj = (JSONObject) parser.parse(rectangle);
						
						for (int i = 0; i < N; i++) {
							
							T dim1 = getInstanceOf();
							T dim2 = getInstanceOf();
							r.setDim1(i, dim1);
							r.setDim2(i, dim2);
							
							switch (i) {
							case 0: 
								r.getDim1(i).setData(rObj.get("x1").toString());
								r.getDim2(i).setData(rObj.get("x2").toString());
								break;
							case 1:
								r.getDim1(i).setData(rObj.get("y1").toString());
								r.getDim2(i).setData(rObj.get("y2").toString());
								break;
							case 2:
								r.getDim1(i).setData(rObj.get("z1").toString());
								r.getDim2(i).setData(rObj.get("z2").toString());
								break;
							default:
								r.getDim1(i).setData(rObj.get(i + "_1").toString());
								r.getDim2(i).setData(rObj.get(i + "_2").toString());
								break;
							}
						}
					} catch (ParseException | IllegalArgumentException | SecurityException e) {
						e.printStackTrace();
					}
				}
				

				returnNode = new RTreeNode<T>(nodeId, children, parent, cache, logger, className);

				returnNode.setRectangle(r);
				returnNode.setLocationItemsJson(items);

			}

			return returnNode;

		} catch (SQLException e) {
			logger.log(e);
			e.printStackTrace();
		}

//		numReads++;
//		readTime += (System.currentTimeMillis() - time);
		performance.addRead(System.currentTimeMillis() - time);

		return returnNode;
	}
	
	@Override
	public void addToMetaData(String treeName, int maxChildren, int maxItems, int N) {
		String query = "INSERT INTO `" + tablePrefix + "_metadata` (`treeName`, `maxChildren`, `maxItems`, `N`, `treeType`) " + "VALUES (?, ?, ?, ?, ?);";

		if (!isDbConnected()) {
			init();
		}
		
		PreparedStatement stmt = null;
		int c = 1;

		try {

			stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(c++, treeName);
			stmt.setInt(c++, maxChildren);
			stmt.setInt(c++, maxItems);
			stmt.setInt(c++, N);
			stmt.setString(c++, className.getSimpleName());
			
			logger.log(stmt.toString(), "QUERY", LogLevel.DEBUG, true);

			stmt.executeUpdate();
		} catch (SQLException e) {
			logger.log(e);
			e.printStackTrace();
		}

	}

	@Override
	public boolean metaDataExists(String treeName) {

		if (!isDbConnected()) {
			init();
		}
		
		String select = " SELECT * FROM `" + tablePrefix + "_metadata` ";
		String where = " WHERE `treeName` = ? ";
		String query = select + where;

		PreparedStatement stmt;
		try {
			stmt = conn.prepareStatement(query);
			stmt.setString(1, treeName);
			
			logger.log(stmt.toString(), "QUERY", LogLevel.DEBUG, true);

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

		if (!isDbConnected()) {
			init();
		}
		
		int maxChildren = 4; // default to 4

		String select = " SELECT * FROM `" + tablePrefix + "_metadata` ";
		String where = " WHERE `treeName` = ? ";
		String query = select + where;

		PreparedStatement stmt;
		try {
			stmt = conn.prepareStatement(query);

			stmt.setString(1, treeName);
			
			logger.log(stmt.toString(), "QUERY", LogLevel.DEBUG, true);

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
		
		if (!isDbConnected()) {
			init();
		}
		
		int maxItems = 4; // default to 4

		String select = " SELECT * FROM `" + tablePrefix + "_metadata` ";
		String where = " WHERE `treeName` = ? ";
		String query = select + where;

		PreparedStatement stmt;
		try {
			stmt = conn.prepareStatement(query);

			stmt.setString(1, treeName);
			
			logger.log(stmt.toString(), "QUERY", LogLevel.DEBUG, true);

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
	public List<T> getMin(String treeName) {
		
		if (!isDbConnected()) {
			init();
		}

		String select = " SELECT * FROM `" + tablePrefix + "_metadata` ";
		String where = " WHERE `treeName` = ? ";
		String query = select + where;
		
		List<T> minimums = new ArrayList<T>();

		PreparedStatement stmt;
		try {
			stmt = conn.prepareStatement(query);

			stmt.setString(1, treeName);
			
			logger.log(stmt.toString(), "QUERY", LogLevel.DEBUG, true);

			ResultSet resultSet = stmt.executeQuery();

			if (resultSet.next()) {
				String minimumsStr = resultSet.getString("minimums");
				
				JSONParser parser = new JSONParser();
				JSONArray arr;
				
				try {
					arr = (JSONArray) parser.parse(minimumsStr);
					
					for (int i = 0; i < arr.size(); i++) {
						T obj = getInstanceOf();
						obj.setData(arr.get(i).toString());
						minimums.add(obj);
					}
					
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}

		} catch (SQLException e) {
			logger.log(e);
			e.printStackTrace();
		}

		return minimums;
	}
	
	@Override
	public List<T> getMax(String treeName) {
		
		if (!isDbConnected()) {
			init();
		}

		String select = " SELECT * FROM `" + tablePrefix + "_metadata` ";
		String where = " WHERE `treeName` = ? ";
		String query = select + where;
		
		List<T> maximums = new ArrayList<T>();

		PreparedStatement stmt;
		try {
			stmt = conn.prepareStatement(query);

			stmt.setString(1, treeName);
			
			logger.log(stmt.toString(), "QUERY", LogLevel.DEBUG, true);

			ResultSet resultSet = stmt.executeQuery();

			if (resultSet.next()) {
				String maximumsStr = resultSet.getString("maximums");
				
				JSONParser parser = new JSONParser();
				JSONArray arr;
				
				try {
					arr = (JSONArray) parser.parse(maximumsStr);
					
					for (int i = 0; i < arr.size(); i++) {
						T obj = getInstanceOf();
						obj.setData(arr.get(i).toString());
						maximums.add(obj);
					}
					
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}

		} catch (SQLException e) {
			logger.log(e);
			e.printStackTrace();
		}

		return maximums;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void updateMetaDataBoundaries(List<T> minimums, List<T> maximums, String treeName) {
		
		if (!isDbConnected()) {
			init();
		}
		
		JSONArray arrMin = new JSONArray();
		JSONArray arrMax = new JSONArray();
		for (int i = 0; i < minimums.size(); i++) {
			arrMin.add(minimums.get(i).getData());
			arrMax.add(maximums.get(i).getData());
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
			
			logger.log(stmt.toString(), "QUERY", LogLevel.DEBUG, true);

			stmt.executeUpdate();

		} catch (SQLException e) {
			logger.log(e);
			e.printStackTrace();
		}
		
	}

	@Override
	public void clearData() {
		
		if (!isDbConnected()) {
			init();
		}
		
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
			
			logger.log(stmt.toString(), "QUERY", LogLevel.DEBUG, true);
			
			stmt.executeUpdate();

		} catch (SQLException e) {
			logger.log(e);
			e.printStackTrace();
		}
		
	}
	
	@Override
	public List<IRTree<T>> getAllTrees() {
		
		if (!isDbConnected()) {
			init();
		}
		
		List<IRTree<T>> trees = new ArrayList<IRTree<T>>();

		String select = " SELECT * FROM `" + tablePrefix + "_metadata` ";
		String where = " WHERE `treeType` = ? ";
		String query = select + where;
		
		int c = 1;

		PreparedStatement stmt;
		try {
			stmt = conn.prepareStatement(query);
			
			stmt.setString(c++, className.getSimpleName());
			
			logger.log(stmt.toString(), "QUERY", LogLevel.DEBUG, true);

			ResultSet resultSet = stmt.executeQuery();

			while (resultSet.next()) {
				String treeName = resultSet.getString("treeName");
				int maxChildren = resultSet.getInt("maxChildren");
				int maxItems = resultSet.getInt("maxItems");
				int N = resultSet.getInt("N");
				
				IRTree<T> tree;
				try {
					tree = new RTree<T>(this, maxChildren, maxItems, logger, N, treeName, className);
					trees.add(tree);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}

		} catch (SQLException e) {
			logger.log(e);
			e.printStackTrace();
		}

		return trees;
	}
	
	@Override
	public List<ILocationItem<T>> getAllLocationItems() {
		
		if (!isDbConnected()) {
			init();
		}
		
		List<ILocationItem<T>> items = new ArrayList<ILocationItem<T>>();

		String select = " SELECT * FROM `" + tablePrefix + "_items` ";
		String where = " WHERE `treeType` = ? ";
		String query = select + where;
		
		int c = 1;

		PreparedStatement stmt;
		try {
			stmt = conn.prepareStatement(query);

			stmt.setString(c++, className.getSimpleName());
			
			logger.log(stmt.toString(), "QUERY", LogLevel.DEBUG, true);
			
			ResultSet resultSet = stmt.executeQuery();

			while (resultSet.next()) {
				
				String id = resultSet.getString("id");
				int N = resultSet.getInt("N");
				String location = resultSet.getString("location");
				String type = resultSet.getString("type");
				
				ILocationItem<T> item;
				try {
					item = new LocationItem<T>(N, id);
					
					JSONParser parser = new JSONParser();
					JSONObject obj = (JSONObject) parser.parse(location);
										
					for (int j = 0; j < N; j++) {

						T val = getInstanceOf();

						switch (j) {
						case 0:
							val.setData(obj.get("x").toString());

							break;
						case 1:
							val.setData(obj.get("y").toString());

							break;
						case 2:
							val.setData(obj.get("z").toString());

							break;
						default:
							val.setData(obj.get(j + "").toString());

							break;
						}

						item.setDim(j, val);
					}
					
					item.setType(type);
					
					items.add(item);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}

		} catch (SQLException e) {
			logger.log(e);
			e.printStackTrace();
		}

		return items;
	}
}
