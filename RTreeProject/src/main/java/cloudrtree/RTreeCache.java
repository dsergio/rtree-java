package cloudrtree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
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
public class RTreeCache {
	
	private Map<String, RTreeNode> cache;
	private IDataStorage dbAccess;
	private String treeName;
	private StorageType cloudType;
	private ILogger logger;
	
	public RTreeCache(String treeName, StorageType cloudType, ILogger logger, IDataStorage dataStorage) throws Exception {
		cache = new HashMap<String, RTreeNode>();
		this.treeName = treeName;
		this.cloudType = cloudType;
		this.logger = logger;
		this.dbAccess = dataStorage;
		
		dbAccess.initializeStorage(treeName);
		
	}
	
	public void printCache() {
		logger.log();
		logger.log("Printing cache:");
		for (String key : cache.keySet()) {
			RTreeNode cloudNode = cache.get(key);
			logger.log(key + ": " + cloudNode);
		}
		logger.log();
	}
	
	public IDataStorage getDBAccess() {
		return dbAccess;
	}
	
	public RTreeNode getNode(String nodeId) {
		
		if (nodeId == null) {
			return null;
		}
		
		if (cache.containsKey(nodeId)) { // first try the cache
			logger.log("returning from cache");
			return cache.get(nodeId);
		} else {		
			RTreeNode node = dbAccess.getCloudRTreeNode(treeName, nodeId, this);
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
		
		
		RTreeNode n = null;
		if (cache.containsKey(nodeId)) {
			
			logger.log("cache contains " + nodeId);
			n = cache.get(nodeId);
			
		} else {
			logger.log("cache DOES NOT contain " + nodeId);
			n = new RTreeNode(nodeId, children, parent, this, logger);
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
	public void addNode(String nodeId, String children, String parent, String items, String rectangle, RTreeNode node) {
		
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
