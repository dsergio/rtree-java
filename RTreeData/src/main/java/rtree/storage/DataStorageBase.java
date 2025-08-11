package rtree.storage;

import rtree.item.IRType;
import rtree.log.ILogger;
import rtree.log.PerformanceMetrics;

/**
 * Base class for data storage implementations, implements IDataStorage interface.
 * @param <T> Type of the items stored in the data storage, extending IRType.
 */
public abstract class DataStorageBase<T extends IRType<T>> implements IDataStorage<T> {
	
	/**
	 * Enum representing the type of storage used.
	 */
	public final StorageType storageType;
	
	/**
	 * Logger for logging messages.
	 */
	protected ILogger logger;
	
	/**
	 * Performance metrics for the data storage operations.
	 */
	protected PerformanceMetrics performance;
	
	/**
	 * Indicates if this is a test instance, which may use a different table prefix.
	 */
	protected boolean isTest;
	
	/**
	 * Prefix for the table names, used to differentiate between different instances
	 * or types of data storage.
	 */
	protected String tablePrefix;
	
	/**
	 * Constructor for DataStorageBase.
	 * 
	 * @param storageType Type of storage (e.g., MYSQL, SQLITE, INMEMORY).
	 * @param logger      Logger instance for logging messages.
	 */
	public DataStorageBase(StorageType storageType, ILogger logger) {
		this.storageType = storageType;
		this.logger = logger;
		this.isTest = false;
		this.tablePrefix = "rtree";
		this.performance = new PerformanceMetrics();
	}
	
	@Override
	public StorageType getStorageType() {
		return storageType;
	}
	
	@Override
	public abstract void init();
	
	@Override
	public abstract int getNumDimensions(String treeName);
	
	/**
	 * Validates the tree name to ensure it contains only valid characters.
	 * @param treeName Name of the R-tree to validate.
	 * @throws IllegalArgumentException if the tree name contains illegal characters.
	 */
	protected void validateTreeName(String treeName) throws IllegalArgumentException {
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
