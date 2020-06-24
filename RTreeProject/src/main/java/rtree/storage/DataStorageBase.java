package rtree.storage;

import rtree.log.ILogger;

public abstract class DataStorageBase implements IDataStorage {
	
	public final StorageType storageType;
	protected ILogger logger;
	protected final String treeName;
	protected int numReads = 0;
	protected int numAdds = 0;
	protected int numUpdates = 0;
	protected long readTime = 0;
	protected long addTime = 0;
	protected long updateTime = 0;
	
	public DataStorageBase(StorageType storageType, ILogger logger, String treeName) {
		this.storageType = storageType;
		this.logger = logger;
		this.treeName = treeName;
		validateTreeName();
	}
	
	public String getTreeName() {
		return treeName;
	}
	
	public StorageType getStorageType() {
		return storageType;
	}
	
	public void validateTreeName() throws IllegalArgumentException {
		if (!treeName.matches("^[a-zA-Z0-9_]+$")) {
			logger.log("Illegal table name");
			throw new IllegalArgumentException(treeName);
		}
	}

}
