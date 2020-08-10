package rtree.storage.generic;

import rtree.item.generic.IRType;
import rtree.log.ILogger;
import rtree.storage.StorageType;

public abstract class DataStorageBaseGeneric<T extends IRType<T>> implements IDataStorageGeneric<T> {

	public final StorageType storageType;
	protected ILogger logger;
//	protected final String treeName;
	protected int numReads = 0;
	protected int numAdds = 0;
	protected int numUpdates = 0;
	protected long readTime = 0;
	protected long addTime = 0;
	protected long updateTime = 0;
//	protected int numDimensions;
	protected boolean isTest;
	protected String tablePrefix;
	
//	public DataStorageBase(StorageType storageType, ILogger logger, String treeName, int numDimensions) {
	public DataStorageBaseGeneric(StorageType storageType, ILogger logger) {
		this.storageType = storageType;
		this.logger = logger;
//		this.treeName = treeName;
//		this.numDimensions = numDimensions;
		this.isTest = false;
		this.tablePrefix = "rtree";
//		validateTreeName();
	}
	
	protected abstract void init();
	
//	public String getTreeName() {
//		return treeName;
//	}
	
	public StorageType getStorageType() {
		return storageType;
	}
	
//	@Override
//	public int getNumDimensions() {
//		return numDimensions;
//	}

	public abstract int getNumDimensions(String treeName);
	
	public void validateTreeName(String treeName) throws IllegalArgumentException {
		if (!treeName.matches("^[a-zA-Z0-9_]+$")) {
			logger.log("Illegal table name");
			throw new IllegalArgumentException(treeName);
		}
	}

}
