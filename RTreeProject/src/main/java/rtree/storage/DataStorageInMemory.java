package rtree.storage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import rtree.log.ILogger;
import rtree.rectangle.Rectangle2D;
import rtree.tree.RTreeCache;
import rtree.tree.RTreeNode;

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
	private int numReads = 0;
	private int numAdds = 0;
	private int numUpdates = 0;
	private long readTime = 0;
	private long addTime = 0;
	private long updateTime = 0;
	private int N;
	
	private Map<String, RTreeNode> localData;
	
	public DataStorageInMemory(ILogger logger, String treeName) {
		super(StorageType.INMEMORY, logger, treeName);
		localData = new HashMap<String, RTreeNode>();
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
	public RTreeNode addCloudRTreeNode(String nodeId, String children, String parent, String items, String rectangle,
			String treeName, RTreeCache cache) {
		
		long time = System.currentTimeMillis();
		logger.log("Calling DBAccessRTreeLocal.addCloudRTreeNode with parameters: ");
		logger.log("nodeId: " + nodeId);
		logger.log("children: " + children);
		logger.log("parent: " + parent);
		logger.log("items: " + items);
		logger.log("rectangle: " + rectangle);
		
		// construct the node
		RTreeNode node = new RTreeNode(nodeId, children, parent, cache, logger);
		
		Rectangle2D r = null;
		JSONParser parser;
		Object obj;
		parser = new JSONParser();
		try {
			if (rectangle != null) {
				obj = parser.parse(rectangle);
				JSONObject rectObj = (JSONObject) obj;
				r = new Rectangle2D(Integer.parseInt(rectObj.get("x1").toString()), 
						Integer.parseInt(rectObj.get("x2").toString()), 
						Integer.parseInt(rectObj.get("y1").toString()),
						Integer.parseInt(rectObj.get("y2").toString()));
			}
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		node.setRectangle(r);
		node.setItemsJson2D(items);
		
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
		
		
		RTreeNode node = localData.get(nodeId);
		
		if (children != null) {
			node.setChildren(children);
		}
		
		node.setParent(parent);
		
		Rectangle2D r = new Rectangle2D();
		
		JSONParser parser;
		Object obj;
		parser = new JSONParser();
		try {
			if (rectangle != null) {
				obj = parser.parse(rectangle);
				JSONObject rectObj = (JSONObject) obj;
				r = new Rectangle2D(Integer.parseInt(rectObj.get("x1").toString()), 
						Integer.parseInt(rectObj.get("x2").toString()), 
						Integer.parseInt(rectObj.get("y1").toString()),
						Integer.parseInt(rectObj.get("y2").toString()));
			}
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if (rectangle != null) {
			node.setRectangle(r);
		}
		if (items != null) {
			node.setItemsJson2D(items);
		}
		
		numUpdates++;
		updateTime += (System.currentTimeMillis() - time);
	}

	@Override
	public RTreeNode getCloudRTreeNode(String tableName, String nodeId, RTreeCache cache) {
		
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
	public void addToMetaDataNDimensional(String treeName, int maxChildren, int maxItems, int N) {
		this.maxChildren = maxChildren;
		this.maxItems = maxItems;
		this.N = N;
		
	}

	@Override
	public int getN(String treeName) {
		// TODO Auto-generated method stub
		return 0;
	}

}
