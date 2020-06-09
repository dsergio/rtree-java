package cloudrtree;

public abstract class DataStorageBase implements IDataStorage {
	
	public final StorageType storageType;
	protected ILogger logger;
	protected final String treeName;
	
	public DataStorageBase(StorageType storageType, ILogger logger, String treeName) {
		this.storageType = storageType;
		this.logger = logger;
		this.treeName = treeName;
		validateTreeName();
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
