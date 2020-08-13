package rtree.storage.generic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rtree.item.ILocationItem;
import rtree.item.generic.ILocationItemGeneric;
import rtree.item.generic.IRType;
import rtree.item.generic.RDouble;
import rtree.item.generic.RInteger;
import rtree.log.ILogger;
import rtree.log.LogLevel;
import rtree.log.LoggerStdOut;
import rtree.storage.generic.DataStorageMySQLGeneric;
import rtree.storage.generic.IDataStorageGeneric;
import rtree.tree.IRTree;
import rtree.tree.generic.IRTreeGeneric;

public class ApplicationDbContextGeneric<T extends IRType<T>> {
	
	private IDataStorageGeneric<T> dataStorage;
	public Map<String, IRTreeGeneric<T>> treeSetMap;
	public Map<String, ILocationItemGeneric<T>> locationItemSetMap;
	
	
	public ApplicationDbContextGeneric(Class<T> clazz) {
		
		ILogger logger = new LoggerStdOut(LogLevel.DEV);
//		dataStorage = new DataStorageSqliteGeneric<T>(logger, clazz);
		dataStorage = new DataStorageMySQLGeneric<T>(logger, clazz);
		List<IRTreeGeneric<T>> treeSet = dataStorage.getAllTrees();
		List<ILocationItemGeneric<T>> locationItemSet = dataStorage.getAllLocationItems();
		treeSetMap = new HashMap<String, IRTreeGeneric<T>>();
		locationItemSetMap = new HashMap<String, ILocationItemGeneric<T>>();
		for (IRTreeGeneric<T> t : treeSet) {
			treeSetMap.put(t.getTreeName(), t);
		}
		for (ILocationItemGeneric<T> i : locationItemSet) {
			locationItemSetMap.put(i.getId(), i);
		}
		
	}
	
	public IDataStorageGeneric<T> getDataStorage() {
		return dataStorage;
	}
	
}
