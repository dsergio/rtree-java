package rtree.storage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import rtree.log.ILogger;
import rtree.rectangle.IHyperRectangle;
import rtree.rectangle.Rectangle2D;
import rtree.rectangle.RectangleND;
import rtree.tree.IRTreeCache;
import rtree.tree.IRTreeNode;
import rtree.tree.RTreeNode2D;
import rtree.tree.RTreeNodeND;

/**
 * 
 * In Memory Storage Implementation
 * 
 * @author David Sergio
 *
 */
public class DataStorageInMemory extends DataStorageBase {
	
	private int maxItems;
	private int maxChildren;
	private Map<String, IRTreeNode> localData;
	
	public DataStorageInMemory(ILogger logger, String treeName, int numDimensions) {
		super(StorageType.INMEMORY, logger, treeName, numDimensions);
		localData = new HashMap<String, IRTreeNode>();
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
	public IRTreeNode addCloudRTreeNode(String nodeId, String children, String parent, String items, String rectangle,
			String treeName, IRTreeCache cache) {
		
		long time = System.currentTimeMillis();
		logger.log("Calling DBAccessRTreeLocal.addCloudRTreeNode with parameters: ");
		logger.log("nodeId: " + nodeId);
		logger.log("children: " + children);
		logger.log("parent: " + parent);
		logger.log("items: " + items);
		logger.log("rectangle: " + rectangle);
		
		// construct the node
		
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
		node.setRectangle(r);
		node.setItemsJson(items);
		
		localData.put(nodeId, node);
		
		numAdds++;
		addTime += (System.currentTimeMillis() - time);
		
		return node;
	}

	@Override
	public void updateItem(String tableName, String nodeId, String children, String parent, String items,
			String rectangle) {
		
		long time = System.currentTimeMillis();
		logger.log("Calling DBAccessRTreeLocal.updateItem with parameters: ");
		logger.log("tableName: " + tableName);
		logger.log("nodeId: " + nodeId);
		logger.log("children: " + children);
		logger.log("parent: " + parent);
		logger.log("items: " + items);
		logger.log("rectangle: " + rectangle);
		
		
		IRTreeNode node = localData.get(nodeId);
		
		if (children != null) {
			node.setChildren(children);
		}
		
		node.setParent(parent);
		
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
	public IRTreeNode getCloudRTreeNode(String tableName, String nodeId, IRTreeCache cache) {
		
		long time = System.currentTimeMillis();
		logger.log("Calling DBAccessRTreeLocal.getCloudRTreeNode with parameters: ");
		logger.log("tableName: " + tableName);
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
	public void updateMetaDataBoundaries(int minX, int maxX, int minY, int maxY) {
		
	}

	@Override
	public void updateMetaDataBoundariesNDimensional(List<Integer> minimums, List<Integer> maximums) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addToMetaDataNDimensional(String treeName, int maxChildren, int maxItems, int numDimensions) {
		this.maxChildren = maxChildren;
		this.maxItems = maxItems;
		this.numDimensions = numDimensions;
		
	}

}
