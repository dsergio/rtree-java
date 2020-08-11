package rtree.storage.generic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import rtree.item.ILocationItem;
import rtree.item.generic.ILocationItemGeneric;
import rtree.item.generic.IRType;
import rtree.log.ILogger;
import rtree.rectangle.IHyperRectangle;
import rtree.rectangle.Rectangle2D;
import rtree.rectangle.RectangleND;
import rtree.rectangle.generic.IHyperRectangleGeneric;
import rtree.rectangle.generic.RectangleNDGeneric;
import rtree.storage.StorageType;
import rtree.tree.IRTree;
import rtree.tree.IRTreeCache;
import rtree.tree.IRTreeNode;
import rtree.tree.RTreeNode2D;
import rtree.tree.RTreeNodeND;
import rtree.tree.generic.IRTreeCacheGeneric;
import rtree.tree.generic.IRTreeGeneric;
import rtree.tree.generic.IRTreeNodeGeneric;
import rtree.tree.generic.RTreeNodeNDGeneric;

/**
 * 
 * In Memory Storage Implementation
 * 
 * @author David Sergio
 *
 */
public class DataStorageInMemoryGeneric<T extends IRType<T>> extends DataStorageBaseGeneric<T> {
	
	private int maxItems;
	private int maxChildren;
	private Map<String, IRTreeNodeGeneric<T>> localData;
	private int numDimensions;
	Class<T> clazz;
	
//	public DataStorageInMemory(ILogger logger, String treeName, int numDimensions) {
	public DataStorageInMemoryGeneric(ILogger logger, Class<T> clazz) {
		super(StorageType.INMEMORY, logger);
		localData = new HashMap<String, IRTreeNodeGeneric<T>>();
		this.clazz = clazz;
	}
	

	@Override
	public void init() {
		maxItems = 4;
		maxChildren = 4;

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
	public IRTreeNodeGeneric<T> addCloudRTreeNode(String nodeId, String children, String parent, String items, String rectangle,
			String treeName, IRTreeCacheGeneric<T> cache) {
		
		long time = System.currentTimeMillis();
		logger.log("Calling DBAccessRTreeLocal.addCloudRTreeNode with parameters: ");
		logger.log("nodeId: " + nodeId);
		logger.log("children: " + children);
		logger.log("parent: " + parent);
		logger.log("items: " + items);
		logger.log("rectangle: " + rectangle);
		
		// construct the node
		
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
					switch (i) {
					case 0: 
						r.setDim1(i, (T) (rObj.get("x1")));
						r.setDim2(i, (T) (rObj.get("x2")));
						break;
					case 1:
						r.setDim1(i, (T) (rObj.get("y1")));
						r.setDim2(i, (T) (rObj.get("y2")));
						break;
					case 2:
						r.setDim1(i, (T) (rObj.get("z1")));
						r.setDim2(i, (T) (rObj.get("z2")));
						break;
					default:
						r.setDim1(i, (T) (rObj.get(i + "_1")));
						r.setDim2(i, (T) (rObj.get(i + "_2")));
						break;
					}
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		node.setRectangle(r);
		node.setItemsJson(items);
		
		localData.put(nodeId, node);
		
		numAdds++;
		addTime += (System.currentTimeMillis() - time);
		
		return node;
	}

	@Override
	public void updateItem(String treeName, String nodeId, String children, String parent, String items,
			String rectangle) {
		
		long time = System.currentTimeMillis();
		logger.log("Calling DBAccessRTreeLocal.updateItem with parameters: ");
		logger.log("tableName: " + treeName);
		logger.log("nodeId: " + nodeId);
		logger.log("children: " + children);
		logger.log("parent: " + parent);
		logger.log("items: " + items);
		logger.log("rectangle: " + rectangle);
		
		
		IRTreeNodeGeneric<T> node = localData.get(nodeId);
		
		if (children != null) {
			node.setChildren(children);
		}
		
		node.setParent(parent);
		
		int numDimensions = getNumDimensions(treeName);
		
		IHyperRectangleGeneric<T> r;
		r = new RectangleNDGeneric<T>(numDimensions);
		
		
		if (rectangle != null) {
			JSONParser parser = new JSONParser();
			JSONObject rObj;
			try {
				rObj = (JSONObject) parser.parse(rectangle);
				for (int i = 0; i < numDimensions; i++) {
					switch (i) {
					case 0: 
						r.setDim1(i, (T) (rObj.get("x1")));
						r.setDim2(i, (T) (rObj.get("x2")));
						break;
					case 1:
						r.setDim1(i, (T) (rObj.get("y1")));
						r.setDim2(i, (T) (rObj.get("y2")));
						break;
					case 2:
						r.setDim1(i, (T) (rObj.get("z1")));
						r.setDim2(i, (T) (rObj.get("z2")));
						break;
					default:
						r.setDim1(i, (T) (rObj.get(i + "_1")));
						r.setDim2(i, (T) (rObj.get(i + "_2")));
						break;
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
			node.setItemsJson(items);
		}
		
		numUpdates++;
		updateTime += (System.currentTimeMillis() - time);
	}

	@Override
	public IRTreeNodeGeneric<T> getCloudRTreeNode(String treeName, String nodeId, IRTreeCacheGeneric<T> cache) {
		
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
	public void addToMetaData(String treeName, int maxChildren, int maxItems) {
		this.maxChildren = maxChildren;
		this.maxItems = maxItems;
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
	public void updateMetaDataBoundaries(int minX, int maxX, int minY, int maxY, String treeName) {
		
	}

	@Override
	public void updateMetaDataBoundariesNDimensional(List<T> minimums, List<T> maximums, String treeName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addToMetaDataNDimensional(String treeName, int maxChildren, int maxItems, int numDimensions) {
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
	public List<IRTreeGeneric<T>> getAllTrees() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<ILocationItemGeneric<T>> getAllLocationItems() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void addItem(String Id, int N, String location, String type, String properties) {
		// TODO Auto-generated method stub
		
	}

}
