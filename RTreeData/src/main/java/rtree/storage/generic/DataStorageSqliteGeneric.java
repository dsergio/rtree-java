package rtree.storage.generic;

import java.io.File;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import rtree.item.generic.IRType;
import rtree.log.ILogger;
import rtree.storage.StorageType;

/**
 * 
 * Sqlite RTree storage implementation
 * 
 * @author David Sergio
 *
 */
public class DataStorageSqliteGeneric<T extends IRType<T>> extends DataStorageSQLBaseGeneric<T> {
	
	private String dir;

//	public DataStorageSqlite(ILogger logger, String treeName, int numDimensions, boolean isTest) {
	public DataStorageSqliteGeneric(ILogger logger, boolean isTest, Class<T> clazz) {
		super(StorageType.SQLITE, logger, clazz);
		if (isTest) {
			this.isTest = true;
			tablePrefix = "rtree_test";
		}
		init();
	}
	
//	public DataStorageSqlite(ILogger logger, String treeName, int numDimensions) {
	public DataStorageSqliteGeneric(ILogger logger, Class<T> clazz) {
		super(StorageType.SQLITE, logger, clazz);
		init();
	}

	@Override
	public void init() {
		
		Configurations configs = new Configurations();
		try {

			Configuration config = configs.properties(new File("resources/config.properties"));
			dir = config.getString("SQLITE.dir");

		} catch (ConfigurationException cex) {
			logger.log(cex);
			cex.printStackTrace();
		}
		
		if (dir == null) {
			dir = System.getProperty("user.dir");
		}

		try {
			Class.forName("org.sqlite.JDBC");
			String connectionString = "jdbc:sqlite:" + dir + java.io.File.separator + "rtree" + ".db";
//			System.out.println("SQLITE CONNECTION STRING: " + connectionString);
			conn = DriverManager.getConnection(connectionString);
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(e);
			System.exit(0);
		}
		logger.log("Opened sqlite database successfully");

	}
	
	@Override
	public void initializeStorage() {

		Statement stmt = null;
		String sql = null;

		try {
			
			sql = " CREATE TABLE IF NOT EXISTS " + tablePrefix + "_metadata" + " (id INTEGER PRIMARY KEY AUTOINCREMENT "
					+ " , treeName VARCHAR(255) NOT NULL " + " , maxChildren INT NULL " + " , maxItems INT NULL "
					+ " , minX INT NULL " + " , maxX INT NULL " + " , minY INT NULL " + " , maxY INT NULL "
					+ " , N INT NULL "  + " , minimums TEXT NULL "  + " , maximums TEXT NULL " 
					+ ")";
			logger.log("create table: \n" + sql);
			
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);

		} catch (SQLException e) {
			logger.log(e);
			e.printStackTrace();
		}
		
		try {
			
			sql = "CREATE TABLE IF NOT EXISTS " + tablePrefix + "_data (nodeId VARCHAR(255) NOT NULL, "
					+ " parent VARCHAR(255) NULL, " + " rectangle TEXT NULL, " + " items TEXT NULL, "
					+ " children TEXT NULL, " + " PRIMARY KEY ( nodeId ))";
			logger.log("create table: \n" + sql);
			
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);

		} catch (SQLException e) {
			logger.log(e);
			e.printStackTrace();
		}
		
		try {
			
			sql = "CREATE TABLE IF NOT EXISTS " + tablePrefix + "_items ("
					+ " id VARCHAR(255) NOT NULL, "
					+ " N INT NOT NULL, " 
					+ " location TEXT NULL, " 
					+ " type VARCHAR(255) NULL, "
					+ " PRIMARY KEY ( id ) "
					+ ")";
			
			logger.log("create table: \n" + sql);
			
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);

		} catch (SQLException e) {
			logger.log(e);
			e.printStackTrace();
		}

	}

	

}
