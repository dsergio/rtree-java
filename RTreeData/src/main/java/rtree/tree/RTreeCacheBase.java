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
	protected IRTree<T> tree;
	protected ILogger logger;
	
	public RTreeCacheBase(IRTree<T> tree, ILogger logger, IDataStorage<T> dataStorage) throws Exception {
		
		cache = new HashMap<String, IRTreeNode<T>>();
		this.tree = tree;
		this.logger = logger;
		this.dbAccess = dataStorage;
		
		dbAccess.initializeStorage();
	}	
	
	public void printCache() {
		logger.log("[CACHE] " + "Printing cache:");
		for (String key : cache.keySet()) {
			IRTreeNode<T> cloudNode = cache.get(key);
			logger.log("[CACHE] " + key + ": " + cloudNode);
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
			return cache.get(nodeId);
		} else {		
			IRTreeNode<T> node = dbAccess.getCloudRTreeNode(tree.getTreeName(), nodeId, this);
			cache.put(nodeId, node);
			
			return node;
		}
	}
	
	@Override
	public void putNode(String nodeId, IRTreeNode<T> node) {
		if (node != null) {
			cache.put(nodeId, node);
			dbAccess.addCloudRTreeNode(node.getNodeId(), node.getChildrenJSON().toString(), node.getParent(),
					node.getLocationItemsJSON().toString(), node.getRectangle().getJson().toString(), tree.getTreeName(), this);
		} else {
			logger.log("[ERROR] RTreeCacheBase.putNode called with null node for " + nodeId);
		}
	}
	
	@Override
	public void updateNode(String nodeId, IRTreeNode<T> node) {
		if (node != null) {
			
			dbAccess.updateItem(tree.getTreeName(), node.getNodeId(), node.getChildrenJSON().toString(), node.getParent(),
					node.getLocationItemsJSON().toString(), node.getRectangle().getJson().toString());
			cache.put(nodeId, node);
		} else {
			logger.log("[ERROR] RTreeCacheBase.updateNode called with null node for " + nodeId);
		}
	}

	public void removeNode(String node) {
		cache.remove(node);
	}
	
	public IRTree<T> getTree() {
		return tree;
	}

}
