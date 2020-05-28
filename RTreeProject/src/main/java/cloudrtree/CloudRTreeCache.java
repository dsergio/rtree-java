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
public class CloudRTreeCache {
	
	private Map<String, CloudRTreeNode> cache;
	private DBAccessRTree dbAccess;
	private String treeName;
	private String cloudType;
	
	public CloudRTreeCache(String treeName, String cloudType) {
		cache = new HashMap<String, CloudRTreeNode>();
		this.treeName = treeName;
		this.cloudType = cloudType;
		
		if (cloudType.equals("DynamoDB")) {
			dbAccess = new DBAccessRTreeDynamoDB();
		} else if (cloudType.equals("Local")) {
			dbAccess = new DBAccessRTreeLocal();
		} else if (cloudType.equals("MySQL")) {
			try {
				dbAccess = new DBAccessRTreeMySQL();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else { // default to MySQL
			try {
				dbAccess = new DBAccessRTreeMySQL();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		dbAccess.createTable(treeName);
	}
	
	public void printCache() {
		System.out.println("Printing cache:");
		for (String key : cache.keySet()) {
			CloudRTreeNode cloudNode = cache.get(key);
			System.out.println(key + ": " + cloudNode);
		}
	}
	
	public DBAccessRTree getDBAccess() {
		return dbAccess;
	}
	
	public CloudRTreeNode getNode(String nodeId) {
		
		if (nodeId == null) {
			return null;
		}
		
		if (cache.containsKey(nodeId)) { // first try the cache
			return cache.get(nodeId);
		} else {		
			CloudRTreeNode node = dbAccess.getCloudRTreeNode(treeName, nodeId, this);
			cache.put(nodeId, node);
			
			return node;
		}
	}
	
	
	public void updateNode(String nodeId, String children, String parent, String items, String rectangle) {
		
		System.out.println("calling CloudRTreeCache.updateNode with parameters: ");
		System.out.println("nodeId: " + nodeId);
		System.out.println("children: " +children);
		System.out.println("parent: " + parent);
		System.out.println("items: " + items);
		System.out.println("rectangle: " + rectangle);
		
		
		CloudRTreeNode n = null;
		if (cache.containsKey(nodeId)) {
			
			System.out.println("cache contains " + nodeId);
			n = cache.get(nodeId);
			
		} else {
			System.out.println("cache DOES NOT contain " + nodeId);
			n = new CloudRTreeNode(nodeId, children, parent, this);
		}
			
		JSONParser parser;
		Object obj;
		
		Rectangle r = new Rectangle();
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

		if (items == null || items.equals("delete")) {
			n.locationItems = new ArrayList<LocationItem>();
		}
		n.setChildren(children);
		n.setRectangle(r);
		n.setParent(parent);
		
		if (items != null && !items.equals("delete")) {
			n.locationItems = new ArrayList<LocationItem>();
			parser = new JSONParser();
			try {
				if (items != null && !items.equals("delete")) {
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
			
		}
		
		cache.put(nodeId, n);
		
		dbAccess.updateItem(treeName, nodeId, children, parent, items, rectangle);	
		
	}
	
	
	public void addNode(String nodeId, String children, String parent, String items, String rectangle, CloudRTreeNode node) {
		
		System.out.println("adding node to cache " + nodeId + " node != null: " + (node != null));
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
