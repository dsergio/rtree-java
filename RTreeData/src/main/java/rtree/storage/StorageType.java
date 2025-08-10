package rtree.storage;

/**
 * Enum representing different storage types for R-Tree data.
 * 
 * This enum defines the various storage options available for R-Tree
 * implementations, allowing for flexibility in how data is stored and accessed.
 */
public enum StorageType {
	
	/**
	 * Represents storage using a MySQL database.
	 */
	MYSQL,
	
	/**
	 * Represents storage using RAM.
	 */
	INMEMORY,
	
	/**
	 * Represents storage using AWS DynamoDB.
	 */
	DYNAMODB,
	
	/**
	 * Represents storage using a file system Sqlite database.
	 */
	SQLITE
}
