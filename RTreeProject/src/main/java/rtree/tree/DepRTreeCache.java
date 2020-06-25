package rtree.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import rtree.item.ILocationItem;
import rtree.item.LocationItem2D;
import rtree.item.LocationItemND;
import rtree.log.ILogger;
import rtree.rectangle.RectangleND;
import rtree.rectangle.IHyperRectangle;
import rtree.rectangle.Rectangle2D;
import rtree.storage.DepIDataStorage;


/**
 * 
 * TODO deprecate
 * 
 * @author David Sergio
 *
 */
public class DepRTreeCache implements DepIRTreeCache {
	
	private Map<String, DepRTreeNode> cache;
	private DepIDataStorage dbAccess;
	private String treeName;
	private ILogger logger;
	private int numDimensions;
	
	public DepRTreeCache(String treeName, ILogger logger, DepIDataStorage dataStorage, int numDimensions) throws Exception {
		cache = new HashMap<String, DepRTreeNode>();
		this.treeName = treeName;
		this.logger = logger;
		this.dbAccess = dataStorage;
		this.numDimensions = numDimensions;
		dbAccess.initializeStorage();
	}
	
	public int getNumDimensions() {
		return numDimensions;
	}
	
	public void printCache() {
		logger.log();
		logger.log("__CACHE: " + "Printing cache:");
		for (String key : cache.keySet()) {
			DepRTreeNode cloudNode = cache.get(key);
			logger.log(key + ": " + cloudNode);
		}
		logger.log();
	}
	
	public DepIDataStorage getDBAccess() {
		return dbAccess;
	}
	
	public DepRTreeNode getNode(String nodeId) {
		
		if (nodeId == null) {
			return null;
		}
		
		if (cache.containsKey(nodeId)) { // first try the cache
//			logger.log("__CACHE: " + "returning " + nodeId + " from cache");
			return cache.get(nodeId);
		} else {		
			DepRTreeNode node = dbAccess.getCloudRTreeNode(treeName, nodeId, this);
			cache.put(nodeId, node);
			
			return node;
		}
	}
	
	
	public void updateNode2D(String nodeId, String children, String parent, String items, String rectangle) {
		
		logger.log("calling CloudRTreeCache.updateNode on " + nodeId + " cacheContains: " + cache.containsKey(nodeId) + ", with parameters: ");
//		logger.log("nodeId: " + nodeId);
//		logger.log("children: " +children);
//		logger.log("parent: " + parent);
//		logger.log("items: " + items);
		logger.log("rectangle: " + rectangle);
		
		
		DepRTreeNode n = null;
		if (cache.containsKey(nodeId)) {
			
//			logger.log("__CACHE: " + "cache contains " + nodeId);
			n = cache.get(nodeId);
			
		} else {
//			logger.log("__CACHE: " + "cache DOES NOT contain " + nodeId);
			n = new DepRTreeNode(nodeId, children, parent, this, logger);
		}
			
		JSONParser parser;
		Object obj;
		
		Rectangle2D r = new Rectangle2D();
		parser = new JSONParser();
		try {
			if (rectangle != null) {
				obj = parser.parse(rectangle);
				JSONObject rectObj = (JSONObject) obj;
				r = new Rectangle2D(
						Integer.parseInt(rectObj.get("x1").toString()), 
						Integer.parseInt(rectObj.get("x2").toString()), 
						Integer.parseInt(rectObj.get("y1").toString()),
						Integer.parseInt(rectObj.get("y2").toString()));
			}
			
			
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		if (children != null) {
			n.setChildren(children);
		}
		if (rectangle != null) {
			n.setRectangle(r);
		}
		if (parent != null) {
			n.setParent(parent);
		}
		
		if (items != null) {
			n.locationItems = new ArrayList<ILocationItem>();
			parser = new JSONParser();
			try {
				if (items != null) {
					obj = parser.parse(items);
					JSONArray arr = (JSONArray) obj;
					for (int i = 0; i < arr.size(); i++) {
						JSONObject row = (JSONObject) arr.get(i);
						ILocationItem item = new LocationItem2D(Integer.parseInt(row.get("x").toString()), Integer.parseInt(row.get("y").toString()), row.get("type").toString());
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
	
	
	public void updateNodeNDimensional(String nodeId, String children, String parent, String items, String rectangle) {
		
		logger.log(numDimensions + "-dimensional CloudRTreeCache.updateNode on " + nodeId + " cacheContains: " + cache.containsKey(nodeId) + ", with parameters: ");
//		logger.log("nodeId: " + nodeId);
//		logger.log("children: " +children);
//		logger.log("parent: " + parent);
//		logger.log("items: " + items);
		logger.log("rectangle: " + rectangle);
		
		
		DepRTreeNode n = null;
		if (cache.containsKey(nodeId)) {
			
//			logger.log("__CACHE: " + "cache contains " + nodeId);
			n = cache.get(nodeId);
			
		} else {
//			logger.log("__CACHE: " + "cache DOES NOT contain " + nodeId);
			n = new DepRTreeNode(nodeId, children, parent, this, logger);
		}
			
		JSONParser parser;
		Object obj;
		
		IHyperRectangle r = new RectangleND(numDimensions);
		parser = new JSONParser();
		try {
			if (rectangle != null) {
				obj = parser.parse(rectangle);
				JSONObject rectObj = (JSONObject) obj;
				
				for (int i = 0; i < numDimensions; i++) {
					
					int value1, value2;
					
					switch (i) {
					case 0: 
						value1 = Integer.parseInt(rectObj.get("x1").toString());
						value2 = Integer.parseInt(rectObj.get("x2").toString());
						break;
					case 1:
						value1 = Integer.parseInt(rectObj.get("y1").toString());
						value2 = Integer.parseInt(rectObj.get("y2").toString());
						break;
					case 2:
						value1 = Integer.parseInt(rectObj.get("z1").toString());
						value2 = Integer.parseInt(rectObj.get("z2").toString());
						break;
					default:
						value1 = Integer.parseInt(rectObj.get(i + "_1").toString());
						value2 = Integer.parseInt(rectObj.get(i + "_2").toString());
						break;
					}
					
					r.setDim1(i, value1);
					r.setDim2(i, value2);
				}
				
			}
			
			
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		if (children != null) {
			n.setChildren(children);
		}
		if (rectangle != null) {
			n.setRectangle(r);
		}
		if (parent != null) {
			n.setParent(parent);
		}
		
		if (items != null) {
			n.locationItems = new ArrayList<ILocationItem>();
			parser = new JSONParser();
			try {
				if (items != null) {
					obj = parser.parse(items);
					JSONArray arr = (JSONArray) obj;
					for (int i = 0; i < arr.size(); i++) {
						JSONObject row = (JSONObject) arr.get(i);
						
						ILocationItem itemND = new LocationItemND(numDimensions);
						
						for (int j = 0; j < numDimensions; j++) {
							
							int value;
							
							switch (j) {
							case 0: 
								value = Integer.parseInt(row.get("x").toString());
								
								break;
							case 1:
								value = Integer.parseInt(row.get("y").toString());
								
								break;
							case 2:
								value = Integer.parseInt(row.get("z").toString());
								
								break;
							default:
								value = Integer.parseInt(row.get(j + "").toString());
								
								break;
							}
							
							itemND.setDim(j, value);
						}
						
						itemND.setType(row.get("type").toString());
						
						n.locationItems.add(itemND);
						
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
	public void addNode(String nodeId, String children, String parent, String items, String rectangle, DepRTreeNode node) {
		
//		logger.log("__CACHE: " + "adding node to cache " + nodeId + " node != null: " + (node != null));
		if (node != null) {
			dbAccess.addCloudRTreeNode(nodeId, node.getChildrenJSON().toString(), node.getParent(), node.getItemsJSON().toString(), node.getRectangle().getJson().toString(), treeName, this);
			cache.put(nodeId, node);
		} else {
			cache.put(nodeId, dbAccess.addCloudRTreeNode(nodeId, children, parent, items, rectangle, treeName, this));
		}
	}
	
	public void addNode(String nodeId, String children, String parent, String items, String rectangle) {
		cache.put(nodeId, dbAccess.addCloudRTreeNode(nodeId, children, parent, items, rectangle, treeName, this));
	}

	public void remove(String node) {
		cache.remove(node);
	}

}
