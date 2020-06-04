package cloudrtree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import cloudrtree.CloudRTree.StorageType;


/**
 * 
 * Description TBD
 * 
 * @author David Sergio
 *
 */
public class CloudRTreeCache {
	
	private Map<String, CloudRTreeNode> cache;
	private DBAccessRTree dbAccess;
	private String treeName;
	private StorageType cloudType;
	private ILogger logger;
	
	public CloudRTreeCache(String treeName, StorageType cloudType, ILogger logger) throws Exception {
		cache = new HashMap<String, CloudRTreeNode>();
		this.treeName = treeName;
		this.cloudType = cloudType;
		this.logger = logger;
		
		switch (cloudType) {
		case MYSQL:
			try {
				dbAccess = new DBAccessRTreeMySQL(logger);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case INMEMORY:
			dbAccess = new DBAccessRTreeInMemory(logger);
			break;
		case DYNAMODB:
			dbAccess = new DBAccessRTreeDynamoDB("us-west-2", logger); // use a static value for now
			break;
		case SQLITE:
			break;
		default:
			try {
				dbAccess = new DBAccessRTreeMySQL(logger);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		dbAccess.initializeStorage(treeName);
		
	}
	
	public void printCache() {
		logger.log();
		logger.log("Printing cache:");
		for (String key : cache.keySet()) {
			CloudRTreeNode cloudNode = cache.get(key);
			logger.log(key + ": " + cloudNode);
		}
		logger.log();
	}
	
	public DBAccessRTree getDBAccess() {
		return dbAccess;
	}
	
	public CloudRTreeNode getNode(String nodeId) {
		
		if (nodeId == null) {
			return null;
		}
		
		if (cache.containsKey(nodeId)) { // first try the cache
			logger.log("returning from cache");
			return cache.get(nodeId);
		} else {		
			CloudRTreeNode node = dbAccess.getCloudRTreeNode(treeName, nodeId, this);
			cache.put(nodeId, node);
			
			return node;
		}
	}
	
	
	public void updateNode(String nodeId, String children, String parent, String items, String rectangle) {
		
		logger.log("calling CloudRTreeCache.updateNode with parameters: ");
		logger.log("nodeId: " + nodeId);
		logger.log("children: " +children);
		logger.log("parent: " + parent);
		logger.log("items: " + items);
		logger.log("rectangle: " + rectangle);
		
		
		CloudRTreeNode n = null;
		if (cache.containsKey(nodeId)) {
			
			logger.log("cache contains " + nodeId);
			n = cache.get(nodeId);
			
		} else {
			logger.log("cache DOES NOT contain " + nodeId);
			n = new CloudRTreeNode(nodeId, children, parent, this, logger);
		}
			
		JSONParser parser;
		Object obj;
		
		Rectangle r = new Rectangle();
		parser = new JSONParser();
		try {
			if (rectangle != null) {
				obj = parser.parse(rectangle);
				JSONObject rectObj = (JSONObject) obj;
				r = new Rectangle(
						Integer.parseInt(rectObj.get("x1").toString()), 
						Integer.parseInt(rectObj.get("x2").toString()), 
						Integer.parseInt(rectObj.get("y1").toString()),
						Integer.parseInt(rectObj.get("y2").toString()));
			}
			
			
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		n.setChildren(children);
		n.setRectangle(r);
		n.setParent(parent);
		
		
		n.locationItems = new ArrayList<LocationItem>();
		parser = new JSONParser();
		try {
			if (items != null) {
				obj = parser.parse(items);
				JSONArray arr = (JSONArray) obj;
				for (int i = 0; i < arr.size(); i++) {
					JSONObject row = (JSONObject) arr.get(i);
					LocationItem item = new LocationItem(Integer.parseInt(row.get("x").toString()), Integer.parseInt(row.get("y").toString()), row.get("type").toString());
					n.locationItems.add(item);
					
				}
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			

		
		cache.put(nodeId, n);
		
		dbAccess.updateItem(treeName, nodeId, children, parent, items, rectangle);	
		
	}
	
	/**
	 * Todo: this method should be polymorphic - one that accepts strings as parameters, 
	 * and another that accepts a node object as a parameter
	 * 
	 * @param nodeId
	 * @param children
	 * @param parent
	 * @param items
	 * @param rectangle
	 * @param node
	 */
	public void addNode(String nodeId, String children, String parent, String items, String rectangle, CloudRTreeNode node) {
		
		logger.log("adding node to cache " + nodeId + " node != null: " + (node != null));
		if (node != null) {
			dbAccess.addCloudRTreeNode(nodeId, node.getChildrenJSON().toString(), node.getParent(), node.getItemsJSON().toString(), node.getRectangle().getJson().toString(), treeName, this);
			cache.put(nodeId, node);
		} else {
			cache.put(nodeId, dbAccess.addCloudRTreeNode(nodeId, children, parent, items, rectangle, treeName, this));
		}
		
	}

	public void remove(String node) {
		cache.remove(node);
	}

}
