package cloudrtree;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * 
 * Description TBD
 * 
 * @author David Sergio
 *
 */
public class DBAccessRTreeInMemory implements DBAccessRTree {
	
	private int maxItems;
	private int maxChildren;
	
	private int numReads = 0;
	private int numAdds = 0;
	private int numUpdates = 0;
	private long readTime = 0;
	private long addTime = 0;
	private long updateTime = 0;
	
	private Map<String, CloudRTreeNode> localData;
	
	private ILogger logger;
	
	public DBAccessRTreeInMemory(ILogger logger) {
		this.logger = logger;
		localData = new HashMap<String, CloudRTreeNode>();
	}

	@Override
	public void init() throws Exception {
		maxItems = 0;
		maxChildren = 0;

	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initializeStorage(String tableName) {
		// TODO Auto-generated method stub
	}

	@Override
	public CloudRTreeNode addCloudRTreeNode(String nodeId, String children, String parent, String items, String rectangle,
			String treeName, CloudRTreeCache cache) {
		
		long time = System.currentTimeMillis();
		logger.log("Calling DBAccessRTreeLocal.addCloudRTreeNode with parameters: ");
		logger.log("nodeId: " + nodeId);
		logger.log("children: " + children);
		logger.log("parent: " + parent);
		logger.log("items: " + items);
		logger.log("rectangle: " + rectangle);
		
		// construct the node
		CloudRTreeNode node = new CloudRTreeNode(nodeId, children, parent, cache, logger);
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
		
		
		CloudRTreeNode node = localData.get(nodeId);
		node.setChildren(children);
		node.setParent(parent);
		
		Rectangle r = new Rectangle();
		
		JSONParser parser;
		Object obj;
		
		parser = new JSONParser();
		try {
			if (rectangle != null) {
				obj = parser.parse(rectangle);
				JSONObject rectObj = (JSONObject) obj;
				r = new Rectangle(Integer.parseInt(rectObj.get("x1").toString()), 
						Integer.parseInt(rectObj.get("x2").toString()), 
						Integer.parseInt(rectObj.get("y1").toString()),
						Integer.parseInt(rectObj.get("y2").toString()));
			}
			
			
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		node.setRectangle(r);
		
		numUpdates++;
		updateTime += (System.currentTimeMillis() - time);
	}

	@Override
	public CloudRTreeNode getCloudRTreeNode(String tableName, String nodeId, CloudRTreeCache cache) {
		
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

}
