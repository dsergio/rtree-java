package cloudrtree;

public abstract class DataStorageBase implements IDataStorage {
	
	public final StorageType storageType;
	
	public DataStorageBase(StorageType storageType) {
		this.storageType = storageType;
	}
	
	public StorageType getStorageType() {
		return storageType;
	}

}
