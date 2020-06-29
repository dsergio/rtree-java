package rtree.storage;

import rtree.log.ILogger;

public abstract class DataStorageBase implements IDataStorage {
	
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
	public DataStorageBase(StorageType storageType, ILogger logger) {
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
