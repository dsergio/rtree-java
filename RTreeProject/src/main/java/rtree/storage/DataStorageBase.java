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
	protected int numDimensions;
	
	public DataStorageBase(StorageType storageType, ILogger logger, String treeName, int numDimensions) {
		this.storageType = storageType;
		this.logger = logger;
		this.treeName = treeName;
		this.numDimensions = numDimensions;
		validateTreeName();
	}
	
	public String getTreeName() {
		return treeName;
	}
	
	public StorageType getStorageType() {
		return storageType;
	}
	
	@Override
	public int getNumDimensions() {
		return numDimensions;
	}
	
	public void validateTreeName() throws IllegalArgumentException {
		if (!treeName.matches("^[a-zA-Z0-9_]+$")) {
			logger.log("Illegal table name");
			throw new IllegalArgumentException(treeName);
		}
	}

}
