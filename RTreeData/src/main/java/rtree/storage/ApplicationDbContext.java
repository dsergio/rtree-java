package rtree.storage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rtree.item.ILocationItem;
import rtree.item.generic.ILocationItemGeneric;
import rtree.item.generic.RDouble;
import rtree.item.generic.RInteger;
import rtree.log.ILogger;
import rtree.log.LogLevel;
import rtree.log.LoggerStdOut;
import rtree.storage.generic.DataStorageMySQLGeneric;
import rtree.storage.generic.IDataStorageGeneric;
import rtree.tree.IRTree;
import rtree.tree.generic.IRTreeGeneric;

public class ApplicationDbContext {
	
	private IDataStorage dataStorage;
	public Map<String, IRTree> treeSetMap;
	public Map<String, ILocationItem> locationItemSetMap;
	
	private IDataStorageGeneric<RDouble> dataStorageDouble;
	public Map<String, IRTreeGeneric<RDouble>> treeSetMapDouble;
	public Map<String, ILocationItemGeneric<RDouble>> locationItemSetMapDouble;
	
	private IDataStorageGeneric<RInteger> dataStorageInteger;
	public Map<String, IRTreeGeneric<RInteger>> treeSetMapInteger;
	public Map<String, ILocationItemGeneric<RInteger>> locationItemSetMapInteger;
	
	public ApplicationDbContext() {
		
		ILogger logger = new LoggerStdOut(LogLevel.DEV);
//		dataStorage = new DataStorageSqlite(logger);
		dataStorage = new DataStorageMySQL(logger);
		List<IRTree> treeSet = dataStorage.getAllTrees();
		List<ILocationItem> locationItemSet = dataStorage.getAllLocationItems();
		treeSetMap = new HashMap<String, IRTree>();
		locationItemSetMap = new HashMap<String, ILocationItem>();
		for (IRTree t : treeSet) {
			treeSetMap.put(t.getTreeName(), t);
		}
		for (ILocationItem i : locationItemSet) {
			locationItemSetMap.put(i.getId(), i);
		}
		
		
		dataStorageDouble = new DataStorageMySQLGeneric<RDouble>(logger, RDouble.class);
		List<IRTreeGeneric<RDouble>> treeSetDouble = dataStorageDouble.getAllTrees();
		List<ILocationItemGeneric<RDouble>> locationItemSetDouble = dataStorageDouble.getAllLocationItems();
		treeSetMapDouble = new HashMap<String, IRTreeGeneric<RDouble>>();
		locationItemSetMapDouble = new HashMap<String, ILocationItemGeneric<RDouble>>();
		
		for (IRTreeGeneric<RDouble> t : treeSetDouble) {
			treeSetMapDouble.put(t.getTreeName(), t);
		}
		for (ILocationItemGeneric<RDouble> i : locationItemSetDouble) {
			locationItemSetMapDouble.put(i.getId(), i);
		}
		
		dataStorageInteger = new DataStorageMySQLGeneric<RInteger>(logger, RInteger.class);
		List<IRTreeGeneric<RInteger>> treeSetInteger = dataStorageInteger.getAllTrees();
		List<ILocationItemGeneric<RInteger>> locationItemSetInteger = dataStorageInteger.getAllLocationItems();
		treeSetMapInteger = new HashMap<String, IRTreeGeneric<RInteger>>();
		locationItemSetMapInteger = new HashMap<String, ILocationItemGeneric<RInteger>>();
		
		for (IRTreeGeneric<RInteger> t : treeSetInteger) {
			treeSetMapInteger.put(t.getTreeName(), t);
		}
		for (ILocationItemGeneric<RInteger> i : locationItemSetInteger) {
			locationItemSetMapInteger.put(i.getId(), i);
		}
	}
	
	public IDataStorage getDataStorage() {
		return dataStorage;
	}
	
	public IDataStorageGeneric<RDouble> getDataStorageDouble() {
		return dataStorageDouble;
	}
	
	public IDataStorageGeneric<RInteger> getDataStorageInteger() {
		return dataStorageInteger;
	}
}
