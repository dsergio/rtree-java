package rtree.storage;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import rtree.log.ILogger;

public class DataStorageMySQL extends DataStorageSQLBase {

	public DataStorageMySQL(ILogger logger, String treeName, int numDimensions) {
		super(StorageType.MYSQL, logger, treeName, numDimensions);
		
	}
	
	@Override
	public void init() {

		String username = "";
		String password = "";
		String host = "";
		String database = "";

		Configurations configs = new Configurations();
		try {

			Configuration config = configs.properties(new File("resources/config.properties"));
			username = config.getString("MYSQL.user");
			password = config.getString("MYSQL.password");
			host = config.getString("MYSQL.host");
			database = config.getString("MYSQL.database");

		} catch (ConfigurationException cex) {
			logger.log(cex);
			cex.printStackTrace();
		}

		String url = "jdbc:mysql://" + host + ":3306/" + database;

		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			logger.log(e);
			e.printStackTrace();
		}

		this.conn = conn;

	}
	
	@Override
	public void initializeStorage() {
		// create metadata table if it doesn't exist
		// metadata table contains the maxChildren and maxItems parameters,
		// and the min and max spatial boundaries

		Statement stmt = null;

		String sql = " CREATE TABLE IF NOT EXISTS rtree_metadata" + " (id INT PRIMARY KEY AUTO_INCREMENT "
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

			sql = "CREATE TABLE IF NOT EXISTS rtree_data (nodeId VARCHAR(255) NOT NULL, "
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
