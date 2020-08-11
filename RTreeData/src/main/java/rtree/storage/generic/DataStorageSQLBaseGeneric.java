package rtree.storage.generic;

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
import rtree.item.LocationItemND;
import rtree.item.generic.ILocationItemGeneric;
import rtree.item.generic.IRType;
import rtree.item.generic.LocationItemNDGeneric;
import rtree.item.generic.RDouble;
import rtree.log.ILogger;
import rtree.rectangle.RectangleND;
import rtree.rectangle.generic.IHyperRectangleGeneric;
import rtree.rectangle.generic.RectangleNDGeneric;
import rtree.storage.StorageType;
import rtree.rectangle.IHyperRectangle;
import rtree.rectangle.Rectangle2D;
import rtree.tree.IRTree;
import rtree.tree.IRTreeCache;
import rtree.tree.IRTreeNode;
import rtree.tree.RTreeND;
import rtree.tree.RTreeNode2D;
import rtree.tree.RTreeNodeND;
import rtree.tree.generic.IRTreeCacheGeneric;
import rtree.tree.generic.IRTreeGeneric;
import rtree.tree.generic.IRTreeNodeGeneric;
import rtree.tree.generic.RTreeNDGeneric;
import rtree.tree.generic.RTreeNodeNDGeneric;

/**
 * 
 * SQL Storage Base Class
 * 
 * @author David Sergio
 *
 */
public abstract class DataStorageSQLBaseGeneric<T extends IRType<T>> extends DataStorageBaseGeneric<T> {

	protected Connection conn;
	Class<T> clazz;
	
//	public DataStorageSQLBase(StorageType storageType, ILogger logger, String treeName, int numDimensions) {
	public DataStorageSQLBaseGeneric(StorageType storageType, ILogger logger, Class<T> clazz) {
		super(storageType, logger);
		this.clazz = clazz;
	}
	
	public T getInstanceOf() {
		
		try {
			
			return clazz.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
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
	public abstract void initializeStorage();

	@Override
	public int getNumDimensions(String treeName) {
		
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

	@SuppressWarnings("unchecked")
	@Override
	public IRTreeNodeGeneric<T> addCloudRTreeNode(String nodeId, String children, String parent, String items, String rectangle,
			String treeName, IRTreeCacheGeneric<T> cache) {

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
		
		int numDimensions = getNumDimensions(treeName);
		
		IRTreeNodeGeneric<T> node = null;
		node = new RTreeNodeNDGeneric<T>(nodeId, children, parent, cache, logger, clazz);
		
		
		IHyperRectangleGeneric<T> r;
		r = new RectangleNDGeneric<T>(numDimensions);
		
		
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
	public void addItem(String Id, int N, String location, String type, String properties) {
		String query = "INSERT INTO `" + tablePrefix + "_items` (`id`, `N`, `location`, `type`, `properties`) "
				+ "VALUES (?, ?, ?, ?, ?);";

		PreparedStatement stmt = null;
		int c = 1;

		try {

			stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

			stmt.setString(c++, Id);
			stmt.setInt(c++, N);
			stmt.setString(c++, location);
			stmt.setString(c++, type);
			stmt.setString(c++, properties);

			logger.log("[QUERY]: " + stmt.toString());

			stmt.executeUpdate();

		} catch (SQLException e) {
			logger.log(e);
			e.printStackTrace();
		}
		
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

	@SuppressWarnings("unchecked")
	@Override
	public IRTreeNodeGeneric<T> getCloudRTreeNode(String tableName, String nodeId, IRTreeCacheGeneric<T> cache) {

		long time = System.currentTimeMillis();
		
		String select = " SELECT * FROM " + tablePrefix + "_data ";

		String where = " WHERE `nodeId` = ?";

		String query = select + where;

		IRTreeNodeGeneric<T> returnNode = null;

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
			
			IHyperRectangleGeneric<T> r = new RectangleNDGeneric<T>(N);
			
//			IHyperRectangle r;
//			if (N == 2) {
//				r = new Rectangle2D();
//			} else {
//				r = new RectangleND(N);
//			}
			
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
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				

				returnNode = new RTreeNodeNDGeneric<T>(nodeId, children, parent, cache, logger, clazz);
				
//				if (cache.getNumDimensions() == 2) {
//					returnNode = new RTreeNode2D(nodeId, children, parent, cache, logger);
//				} else {
//					returnNode = new RTreeNodeND(nodeId, children, parent, cache, logger);
//				}

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
	public List<T> getMin(String treeName) {

		String select = " SELECT * FROM `" + tablePrefix + "_metadata` ";
		String where = " WHERE `treeName` = ? ";
		String query = select + where;
		
		List<T> minimums = new ArrayList<T>();

		PreparedStatement stmt;
		try {
			stmt = conn.prepareStatement(query);

			stmt.setString(1, treeName);

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

		String select = " SELECT * FROM `" + tablePrefix + "_metadata` ";
		String where = " WHERE `treeName` = ? ";
		String query = select + where;
		
		List<T> maximums = new ArrayList<T>();

		PreparedStatement stmt;
		try {
			stmt = conn.prepareStatement(query);

			stmt.setString(1, treeName);

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
	

	@Override
	@Deprecated
	public void updateMetaDataBoundaries(int minX, int maxX, int minY, int maxY, String treeName) {
		
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
	public void updateMetaDataBoundariesNDimensional(List<T> minimums, List<T> maximums, String treeName) {
		
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
	
	@Override
	public List<IRTreeGeneric<T>> getAllTrees() {
		
		List<IRTreeGeneric<T>> trees = new ArrayList<IRTreeGeneric<T>>();

		String select = " SELECT * FROM `" + tablePrefix + "_metadata` ";
		String query = select;

		PreparedStatement stmt;
		try {
			stmt = conn.prepareStatement(query);

			ResultSet resultSet = stmt.executeQuery();

			while (resultSet.next()) {
				String treeName = resultSet.getString("treeName");
				int maxChildren = resultSet.getInt("maxChildren");
				int maxItems = resultSet.getInt("maxItems");
				int N = resultSet.getInt("N");
				
				IRTreeGeneric<T> tree;
				try {
					tree = new RTreeNDGeneric<T>(this, maxChildren, maxItems, logger, N, treeName, clazz);
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
	public List<ILocationItemGeneric<T>> getAllLocationItems() {
		List<ILocationItemGeneric<T>> items = new ArrayList<ILocationItemGeneric<T>>();

		String select = " SELECT * FROM `" + tablePrefix + "_items` ";
		String query = select;

		PreparedStatement stmt;
		try {
			stmt = conn.prepareStatement(query);

			ResultSet resultSet = stmt.executeQuery();

			while (resultSet.next()) {
				
				String id = resultSet.getString("id");
				int N = resultSet.getInt("N");
				String location = resultSet.getString("location");
				String type = resultSet.getString("type");
				
				ILocationItemGeneric<T> item;
				try {
					item = new LocationItemNDGeneric<T>(N, id);
					
					JSONParser parser = new JSONParser();
					JSONObject obj = (JSONObject) parser.parse(location);
										
					for (int j = 0; j < N; j++) {

						T value;

						switch (j) {
						case 0:
							value = (T) (obj.get("x"));

							break;
						case 1:
							value = (T) (obj.get("y"));

							break;
						case 2:
							value = (T) (obj.get("z"));

							break;
						default:
							value = (T) (obj.get(j + ""));

							break;
						}

						item.setDim(j, value);
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
