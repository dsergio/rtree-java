package rtree.storage;

import java.util.List;

import rtree.log.ILogger;
import rtree.log.LogLevel;
import rtree.log.LoggerStdOut;
import rtree.tree.IRTree;

public class ApplicationDbContext {
	
	private IDataStorage dataStorage;
	
	public List<IRTree> treeSet;
	
	public ApplicationDbContext() {
		
		ILogger logger = new LoggerStdOut(LogLevel.DEV);
		dataStorage = new DataStorageSqlite(logger);
		treeSet = dataStorage.getAllTrees(dataStorage);
	}
	
	
	
}
