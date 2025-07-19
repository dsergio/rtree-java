package rtree.tree;

import java.util.HashMap;
import java.util.Map;

import rtree.item.IRType;
import rtree.log.ILogger;
import rtree.storage.IDataStorage;


/**
 * 
 * R-Tree Cache Base Class
 * 
 * @author David Sergio
 *
 */
public abstract class RTreeCacheBase<T extends IRType<T>> implements IRTreeCache<T> {
	
	protected Map<String, IRTreeNode<T>> cache;
	protected IDataStorage<T> dbAccess;
	protected String treeName;
	protected ILogger logger;
	protected int numDimensions;
	
	public RTreeCacheBase(String treeName, ILogger logger, IDataStorage<T> dataStorage, int numDimensions) throws Exception {
		cache = new HashMap<String, IRTreeNode<T>>();
		this.treeName = treeName;
		this.logger = logger;
		this.dbAccess = dataStorage;
		this.numDimensions = numDimensions;
		dbAccess.initializeStorage();
	}
	
	public abstract void updateNode(String nodeId, String children, String parent, String items, String rectangle);
	
	public int getNumDimensions() {
		return numDimensions;
	}
	
	public void printCache() {
		logger.log();
		logger.log("__CACHE: " + "Printing cache:");
		for (String key : cache.keySet()) {
			IRTreeNode<T> cloudNode = cache.get(key);
			logger.log(key + ": " + cloudNode);
		}
		logger.log();
	}
	
	public IDataStorage<T> getDBAccess() {
		return dbAccess;
	}
	
	public IRTreeNode<T> getNode(String nodeId) {
		
		if (nodeId == null) {
			return null;
		}
		
		if (cache.containsKey(nodeId)) { // first try the cache
//			logger.log("__CACHE: " + "returning " + nodeId + " from cache");
			return cache.get(nodeId);
		} else {		
			IRTreeNode<T> node = dbAccess.getCloudRTreeNode(treeName, nodeId, this);
			cache.put(nodeId, node);
			
			return node;
		}
	}
	
	/**
	 * TODO this method should be polymorphic - one that accepts strings as parameters, 
	 * and another that accepts a node object as a parameter
	 * 
	 * @param nodeId
	 * @param children
	 * @param parent
	 * @param items
	 * @param rectangle
	 * @param node
	 */
	public void addNode(String nodeId, String children, String parent, String items, String rectangle, IRTreeNode<T> node) {
		
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
