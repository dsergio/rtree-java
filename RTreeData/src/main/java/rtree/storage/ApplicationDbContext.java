package rtree.storage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rtree.item.ILocationItem;
import rtree.item.IRType;
import rtree.log.ILogger;
import rtree.log.LogLevel;
import rtree.log.LoggerStdOut;
import rtree.tree.IRTree;

/**
 * ApplicationDbContext is a context class that manages the data storage and
 * provides access to R-trees and location items. It initializes the data
 * storage and loads all trees and location items into maps for easy access.
 * 
 * @param <T> Type of the data enclosed in the type.
 */
public class ApplicationDbContext<T extends IRType<T>> {
	
	private IDataStorage<T> dataStorage;
	
	/**
	 * A map of R-trees indexed by their names. This allows quick access to R-trees
	 * by their unique name.
	 */
	public Map<String, IRTree<T>> treeSetMap;
	
	/**
	 * A map of location items indexed by their ID. This allows quick access to
	 * location items by their unique identifier.
	 */
	public Map<String, ILocationItem<T>> locationItemSetMap;
	
	
	/**
	 * Constructor for ApplicationDbContext.
	 * @param className The class type of the items stored in the R-tree, extending IRType.
	 */
	public ApplicationDbContext(Class<T> className) {
		
		ILogger logger = new LoggerStdOut(LogLevel.DEBUG);
		dataStorage = new DataStorageMySQL<T>(logger, className);
		
		List<IRTree<T>> treeSet = dataStorage.getAllTrees();
		List<ILocationItem<T>> locationItemSet = dataStorage.getAllLocationItems();
		treeSetMap = new HashMap<String, IRTree<T>>();
		locationItemSetMap = new HashMap<String, ILocationItem<T>>();
		for (IRTree<T> t : treeSet) {
			treeSetMap.put(t.getTreeName(), t);
		}
		for (ILocationItem<T> i : locationItemSet) {
			locationItemSetMap.put(i.getId(), i);
		}
		
	}
	
	/**
	 * Get the data storage instance.
	 * @return the data storage instance
	 */
	public IDataStorage<T> getDataStorage() {
		return dataStorage;
	}
	
}
