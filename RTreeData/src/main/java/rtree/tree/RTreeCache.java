package rtree.tree;

import java.lang.reflect.InvocationTargetException;
import rtree.item.IRType;
import rtree.log.ILogger;
import rtree.storage.IDataStorage;

/**
 * 
 * RTreeCache class represents a cache for R-tree nodes.
 * @param <T> {@link rtree.item.IRType}
 *
 */
public class RTreeCache<T extends IRType<T>> extends RTreeCacheBase<T> {

	Class<T> className;
	
	/**
	 * Constructor for RTreeCache.
	 * @param tree The R-tree structure to be cached.
	 * @param logger Logger instance for logging operations.
	 * @param dataStorage Data storage instance for persisting R-tree nodes.
	 * @param className Class type of the items stored in the R-tree, extending IRType.
	 * @throws Exception if there is an error initializing the data storage.
	 */
	public RTreeCache(IRTree<T> tree, ILogger logger, IDataStorage<T> dataStorage, Class<T> className) throws Exception {
		super(tree, logger, dataStorage);
		this.className = className;
	}
	
	/**
	 * Creates an instance of the class type specified.
	 * 
	 * @return a new instance of the class type
	 */
	public T getInstanceOf() {
		
		try {
			return className.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			logger.log(e);
		}
		
		return null;
	}

}
