package rtree.storage;

import java.util.List;

import rtree.item.ILocationItem;
import rtree.log.ILogger;
import rtree.log.LogLevel;
import rtree.log.LoggerStdOut;
import rtree.tree.IRTree;

public class ApplicationDbContext {
	
	private IDataStorage dataStorage;
	
	public List<IRTree> treeSet;
	public List<ILocationItem> locationItemSet;
	
	public ApplicationDbContext() {
		
		ILogger logger = new LoggerStdOut(LogLevel.DEV);
		dataStorage = new DataStorageSqlite(logger);
		
		treeSet = dataStorage.getAllTrees();
		locationItemSet = dataStorage.getAllLocationItems();
	}
	
	public void save() {
		
	}
	
}
