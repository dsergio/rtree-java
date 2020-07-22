package rtree.storage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rtree.item.ILocationItem;
import rtree.log.ILogger;
import rtree.log.LogLevel;
import rtree.log.LoggerStdOut;
import rtree.tree.IRTree;

public class ApplicationDbContext {
	
	private IDataStorage dataStorage;
	
	public Map<String, IRTree> treeSetMap;
	public Map<String, ILocationItem> locationItemSetMap;
	
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
	}
	
	public IDataStorage getDataStorage() {
		return dataStorage;
	}
}
