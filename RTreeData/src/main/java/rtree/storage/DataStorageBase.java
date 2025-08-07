package rtree.storage;

import rtree.item.IRType;
import rtree.log.ILogger;
import rtree.log.PerformanceMetrics;

/**
 * Base class for data storage implementations, implements IDataStorage interface.
 * @param <T> Type of the items stored in the data storage, extending IRType.
 */
public abstract class DataStorageBase<T extends IRType<T>> implements IDataStorage<T> {

	public final StorageType storageType;
	protected ILogger logger;
	
	protected PerformanceMetrics performance;
	
	protected boolean isTest;
	protected String tablePrefix;
	
	public DataStorageBase(StorageType storageType, ILogger logger) {
		this.storageType = storageType;
		this.logger = logger;
		this.isTest = false;
		this.tablePrefix = "rtree";
		this.performance = new PerformanceMetrics();
	}
	
	protected abstract void init();
	
	public StorageType getStorageType() {
		return storageType;
	}

	public abstract int getNumDimensions(String treeName);
	
	public void validateTreeName(String treeName) throws IllegalArgumentException {
		if (!treeName.matches("^[a-zA-Z0-9_]+$")) {
			logger.log("Illegal table name", "validateTreeName", rtree.log.LogLevel.ERROR, true);
			throw new IllegalArgumentException(treeName);
		}
	}
	
	@Override
	public PerformanceMetrics getPerformance() {
		return performance;
	}

}
