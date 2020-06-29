package rtree.storage;

import java.io.File;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import rtree.log.ILogger;

/**
 * 
 * Sqlite RTree storage implementation
 * 
 * @author David Sergio
 *
 */
public class DataStorageSqlite extends DataStorageSQLBase {
	
	private String dir;

//	public DataStorageSqlite(ILogger logger, String treeName, int numDimensions, boolean isTest) {
	public DataStorageSqlite(ILogger logger, boolean isTest) {
		super(StorageType.SQLITE, logger);
		if (isTest) {
			this.isTest = true;
			tablePrefix = "rtree_test";
		}
		init();
	}
	
//	public DataStorageSqlite(ILogger logger, String treeName, int numDimensions) {
	public DataStorageSqlite(ILogger logger) {
		super(StorageType.SQLITE, logger);
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
		// create metadata table if it doesn't exist
		// metadata table contains the maxChildren and maxItems parameters,
		// and the min and max spatial boundaries

		Statement stmt = null;

		String sql = " CREATE TABLE IF NOT EXISTS " + tablePrefix + "_metadata" + " (id INTEGER PRIMARY KEY AUTOINCREMENT "
				+ " , treeName VARCHAR(255) NOT NULL " + " , maxChildren INT NULL " + " , maxItems INT NULL "
				+ " , minX INT NULL " + " , maxX INT NULL " + " , minY INT NULL " + " , maxY INT NULL "
				+ " , N INT NULL "  + " , minimums TEXT NULL "  + " , maximums TEXT NULL " 
				+ ")";
		logger.log("create table: \n" + sql);

		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);

			// create data table if it doesn't exist
			// all trees in one table 'rtree_data'

			stmt = conn.createStatement();

			sql = "CREATE TABLE IF NOT EXISTS " + tablePrefix + "_data (nodeId VARCHAR(255) NOT NULL, "
					+ " parent VARCHAR(255) NULL, " + " rectangle TEXT NULL, " + " items TEXT NULL, "
					+ " children TEXT NULL, " + " PRIMARY KEY ( nodeId ))";
			logger.log("create table: \n" + sql);

			stmt.executeUpdate(sql);

		} catch (SQLException e) {
			logger.log(e);
			e.printStackTrace();
		}

	}

	

}
