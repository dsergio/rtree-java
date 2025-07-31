package rtree.storage;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import rtree.item.ILocationItem;
import rtree.item.IRType;
import rtree.log.ILogger;
import rtree.rectangle.IHyperRectangle;
import rtree.rectangle.HyperRectangle;
import rtree.tree.IRTreeCache;
import rtree.tree.IRTree;
import rtree.tree.IRTreeNode;
import rtree.tree.RTreeNode;

/**
 * 
 * In Memory Storage Implementation
 * 
 * @author David Sergio
 *
 */
public class DataStorageInMemory<T extends IRType<T>> extends DataStorageBase<T> {
	
	private int maxItems;
	private int maxChildren;
	private Map<String, IRTreeNode<T>> localData;
	private int numDimensions;
	Class<T> className;
	
	public DataStorageInMemory(ILogger logger, Class<T> className) {
		super(StorageType.INMEMORY, logger);
		localData = new HashMap<String, IRTreeNode<T>>();
		this.className = className;
	}
	

	@Override
	public void init() {
		maxItems = 4;
		maxChildren = 4;

	}

	@Override
	public void close() {

	}

	@Override
	public void initializeStorage() {
	}

	@Override
	public IRTreeNode<T> addRTreeNode(String nodeId, String children, String parent, String items, String rectangle,
			String treeName, IRTreeCache<T> cache) {
		
		long time = System.currentTimeMillis();
		logger.log("Calling DBAccessRTreeLocal.addCloudRTreeNode with parameters: ");
		logger.log("nodeId: " + nodeId);
		logger.log("children: " + children);
		logger.log("parent: " + parent);
		logger.log("items: " + items);
		logger.log("rectangle: " + rectangle);
		
		// construct the node
		
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
					
					
					String dim1;
					String dim2;
					
					switch (i) {
					case 0: 
						dim1 = "x1";
						dim2 = "x2";
//						r.setDim1(i, (T) (rObj.get("x1")));
//						r.setDim2(i, (T) (rObj.get("x2")));
						break;
					case 1:
						dim1 = "y1";
						dim2 = "y2";
//						r.setDim1(i, (T) (rObj.get("y1")));
//						r.setDim2(i, (T) (rObj.get("y2")));
						break;
					case 2:
						dim1 = "z1";
						dim2 = "z2";
//						r.setDim1(i, (T) (rObj.get("z1")));
//						r.setDim2(i, (T) (rObj.get("z2")));
						break;
					default:
						dim1 = i + "_1";
						dim2 = i + "_2";
//						r.setDim1(i, (T) (rObj.get(i + "_1")));
//						r.setDim2(i, (T) (rObj.get(i + "_2")));
						break;
					}
					
					try {
						T t_dim1 = (T) className.getDeclaredConstructor().newInstance();
						T t_dim2 = (T) className.getDeclaredConstructor().newInstance();
						t_dim1.setData((String) rObj.get(dim1));
						t_dim2.setData((String) rObj.get(dim2));
						r.setDim1(i, t_dim1);
						r.setDim2(i, t_dim2);
					} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
							| NoSuchMethodException | SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			} catch (ParseException e) {
				logger.log("Error parsing rectangle JSON: " + rectangle);
				e.printStackTrace();
			}
		}
		node.setRectangle(r);
		node.setLocationItemsJson(items);
		
		localData.put(nodeId, node);
		
		numAdds++;
		addTime += (System.currentTimeMillis() - time);
		
		return node;
	}

	@Override
	public void updateRTreeNode(String treeName, String nodeId, String children, String parent, String items,
			String rectangle) {
		
		long time = System.currentTimeMillis();
		logger.log("Calling DBAccessRTreeLocal.updateItem with parameters: ");
		logger.log("tableName: " + treeName);
		logger.log("nodeId: " + nodeId);
		logger.log("children: " + children);
		logger.log("parent: " + parent);
		logger.log("items: " + items);
		logger.log("rectangle: " + rectangle);
		
		
		IRTreeNode<T> node = localData.get(nodeId);
		
		if (node == null) {
			logger.log("[ERROR] Node with ID " + nodeId + " not found in local data.");
			return;
		}
		
		if (children != null) {
			node.setChildrenJSON(children);
		}
		
		node.setParent(parent);
		
		int numDimensions = getNumDimensions(treeName);
		
		IHyperRectangle<T> r;
		r = new HyperRectangle<T>(numDimensions);
		
		
		if (rectangle != null) {
			JSONParser parser = new JSONParser();
			JSONObject rObj;
			try {
				rObj = (JSONObject) parser.parse(rectangle);
				for (int i = 0; i < numDimensions; i++) {
					
					String dim1;
					String dim2;
					
					switch (i) {
					case 0: 
						dim1 = "x1";
						dim2 = "x2";
//						r.setDim1(i, (T) (rObj.get("x1")));
//						r.setDim2(i, (T) (rObj.get("x2")));
						break;
					case 1:
						dim1 = "y1";
						dim2 = "y2";
//						r.setDim1(i, (T) (rObj.get("y1")));
//						r.setDim2(i, (T) (rObj.get("y2")));
						break;
					case 2:
						dim1 = "z1";
						dim2 = "z2";
//						r.setDim1(i, (T) (rObj.get("z1")));
//						r.setDim2(i, (T) (rObj.get("z2")));
						break;
					default:
						dim1 = i + "_1";
						dim2 = i + "_2";
//						r.setDim1(i, (T) (rObj.get(i + "_1")));
//						r.setDim2(i, (T) (rObj.get(i + "_2")));
						break;
					}
					
					try {
						T t_dim1 = (T) className.getDeclaredConstructor().newInstance();
						T t_dim2 = (T) className.getDeclaredConstructor().newInstance();
						t_dim1.setData((String) rObj.get(dim1));
						t_dim2.setData((String) rObj.get(dim2));
						r.setDim1(i, t_dim1);
						r.setDim2(i, t_dim2);
					} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
							| NoSuchMethodException | SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (rectangle != null) {
			node.setRectangle(r);
		}
		if (items != null) {
			node.setLocationItemsJson(items);
		}
		
		numUpdates++;
		updateTime += (System.currentTimeMillis() - time);
	}

	@Override
	public IRTreeNode<T> getRTreeNode(String treeName, String nodeId, IRTreeCache<T> cache) {
		
		long time = System.currentTimeMillis();
		logger.log("Calling DBAccessRTreeLocal.getCloudRTreeNode with parameters: ");
		logger.log("treeName: " + treeName);
		logger.log("nodeId: " + nodeId);
		
		numReads++;
		readTime += (System.currentTimeMillis() - time);
		
		return localData.get(nodeId);
		
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
	public boolean metaDataExists(String treeName) {
		return maxChildren != 0 && maxItems != 0;
	}

	@Override
	public int getMaxChildren(String treeName) {
		return maxChildren;
	}

	@Override
	public int getMaxItems(String treeName) {
		return maxItems;
	}

	@Override
	public void updateMetaDataBoundaries(List<T> minimums, List<T> maximums, String treeName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addToMetaData(String treeName, int maxChildren, int maxItems, int numDimensions) {
		this.maxChildren = maxChildren;
		this.maxItems = maxItems;
		this.numDimensions = numDimensions;
		
	}


	@Override
	public void clearData() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public int getNumDimensions(String treeName) {
		return numDimensions;
	}


	@Override
	public List<IRTree<T>> getAllTrees() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<ILocationItem<T>> getAllLocationItems() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void addLocationItem(String Id, int N, String location, String type, String properties) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public List<T> getMin(String treeName) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<T> getMax(String treeName) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean isDbConnected() {
		// TODO Auto-generated method stub
		return true;
	}

}
