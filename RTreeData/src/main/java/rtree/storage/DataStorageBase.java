package rtree.storage;

import rtree.item.IRType;
import rtree.log.ILogger;

public abstract class DataStorageBase<T extends IRType<T>> implements IDataStorage<T> {

	public final StorageType storageType;
	protected ILogger logger;
	protected int numReads = 0;
	protected int numAdds = 0;
	protected int numUpdates = 0;
	protected long readTime = 0;
	protected long addTime = 0;
	protected long updateTime = 0;
	protected boolean isTest;
	protected String tablePrefix;
	
	public DataStorageBase(StorageType storageType, ILogger logger) {
		this.storageType = storageType;
		this.logger = logger;
		this.isTest = false;
		this.tablePrefix = "rtree";
	}
	
	protected abstract void init();
	
	public StorageType getStorageType() {
		return storageType;
	}

	public abstract int getNumDimensions(String treeName);
	
	public void validateTreeName(String treeName) throws IllegalArgumentException {
		if (!treeName.matches("^[a-zA-Z0-9_]+$")) {
			logger.log("Illegal table name");
			throw new IllegalArgumentException(treeName);
		}
	}

}
