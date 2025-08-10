package rtree.tree;

import java.util.HashMap;
import java.util.Map;

import rtree.item.IRType;
import rtree.log.ILogger;
import rtree.log.LogLevel;
import rtree.storage.IDataStorage;


/**
 * 
 * R-Tree Cache Base Class
 * Next Steps: Implement a cache eviction policy (e.g., LRU, LFU) to manage the cache size.
 * 
 * @author David Sergio
 * @param <T> {@link rtree.item.IRType}
 */
public abstract class RTreeCacheBase<T extends IRType<T>> implements IRTreeCache<T> {
	
	/**
	 * Cache for R-tree nodes, mapping node IDs to their corresponding IRTreeNode
	 * instances.
	 */
	protected Map<String, IRTreeNode<T>> cache;
	
	/**
	 * Data storage access object for R-tree nodes. This is used to persistently
	 * store and retrieve R-tree nodes.
	 */
	protected IDataStorage<T> dbAccess;
	
	/**
	 * The R-tree instance associated with this cache.
	 */
	protected IRTree<T> tree;
	
	/**
	 * Logger instance for logging operations related to the R-tree cache.
	 */
	protected ILogger logger;
	
	/**
	 * Constructor for the RTreeCacheBase class.
	 * 
	 * @param tree        The R-tree structure to be cached.
	 * @param logger      Logger instance for logging operations.
	 * @param dataStorage Data storage instance for persisting R-tree nodes.
	 * @throws Exception if there is an error initializing the data storage.
	 */
	public RTreeCacheBase(IRTree<T> tree, ILogger logger, IDataStorage<T> dataStorage) throws Exception {
		
		cache = new HashMap<String, IRTreeNode<T>>();
		this.tree = tree;
		this.logger = logger;
		this.dbAccess = dataStorage;
		
		dbAccess.initializeStorage();
	}	
	
	@Override
	public void printCache() {
		logger.log("Printing cache:", "cache", LogLevel.DEBUG, true);
		for (String key : cache.keySet()) {
			IRTreeNode<T> cloudNode = cache.get(key);
			logger.log(key + ": " + cloudNode, "cache", LogLevel.DEBUG, true);
		}
		logger.log();
	}
	
	@Override
	public IDataStorage<T> getDBAccess() {
		return dbAccess;
	}
	
	@Override
	public IRTreeNode<T> getNode(String nodeId) {
		
		if (nodeId == null) {
			return null;
		}
		
		if (cache.containsKey(nodeId)) { // first try the cache
			return cache.get(nodeId);
			
		} else {
			
			IRTreeNode<T> node = dbAccess.getRTreeNode(tree.getTreeName(), nodeId, this);
			
			if (node != null) {
				cache.put(nodeId, node);
			} else {
				logger.log("RTreeCacheBase.getNode called with unknown nodeId: " + nodeId + " and returned null.",
						"getNode", LogLevel.ERROR, true);
				return null;
			}
			
			return node;
		}
	}
	
	@Override
	public void putNode(String nodeId, IRTreeNode<T> node) {
		if (node != null) {
			
			if (getNode(nodeId) != null) {
				
				updateNode(nodeId, node);
				
			} else {
			
				cache.put(nodeId, node);
				dbAccess.addRTreeNode(node.getNodeId(), node.getChildrenJSON().toString(), node.getParent(),
						node.getLocationItemsJSON().toString(), node.getRectangle().getJson().toString(), tree.getTreeName(), this);
				
			}
		} else {
			logger.log("RTreeCacheBase.putNode called with null node for " + nodeId, "putNode", LogLevel.ERROR,
					true);
		}
	}
	
	@Override
	public void updateNode(String nodeId, IRTreeNode<T> node) {
		if (node != null) {
			
			dbAccess.updateRTreeNode(tree.getTreeName(), node.getNodeId(), node.getChildrenJSON().toString(), node.getParent(),
					node.getLocationItemsJSON().toString(), node.getRectangle().getJson().toString());
			cache.put(nodeId, node);
		} else {
			logger.log("RTreeCacheBase.updateNode called with null node for " + nodeId, "updateNode",
					LogLevel.ERROR, true);
		}
	}
	
	@Override
	public void removeNode(String node) {
		cache.remove(node);
	}
	
	@Override
	public IRTree<T> getTree() {
		return tree;
	}

}
