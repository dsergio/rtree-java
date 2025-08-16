package rtree.storage;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import rtree.item.IRType;
import rtree.log.ILogger;
import rtree.log.LogLevel;

/**
 * MySQL RTree storage implementation.
 * @param <T> Type of the items stored in the R-tree, extending IRType.
 */
public class DataStorageMySQL<T extends IRType<T>> extends DataStorageSQLBase<T> {

	/**
	 * Constructor for DataStorageMySQL.
	 * 
	 * @param logger    Logger instance for logging messages.
	 * @param className Class type of the items stored in the R-tree, extending
	 *                  IRType.
	 */
	public DataStorageMySQL(ILogger logger, Class<T> className) {
		super(StorageType.MYSQL, logger, className);
		init();
	}
	
	@Override
	public void init() {

		String username = "";
		String password = "";
		String host = "";
		String database = "";
		
		if (System.getenv("MYSQL_user") != null && 
			System.getenv("MYSQL_password") != null && 
			System.getenv("MYSQL_host") != null && 
			System.getenv("MYSQL_database") != null
			) {
			username = System.getenv("MYSQL_user");
			password = System.getenv("MYSQL_password");
			host = System.getenv("MYSQL_host");
			database = System.getenv("MYSQL_database");
			
			
		} else {
			
//			System.out.println("Using config.properties file for MySQL connection parameters MYSQL_user: (" + System.getenv("MYSQL_user") + ")");
//			System.getenv().forEach((k, v) -> System.out.println(k + ": " + v));

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
		}

		String url = "jdbc:mysql://" + host + ":3306/" + database + "?serverTimezone=UTC";
		
		

		Connection conn = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			logger.log(e);
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
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

		String sql = " CREATE TABLE IF NOT EXISTS " + tablePrefix + "_metadata" + " (id INT PRIMARY KEY AUTO_INCREMENT "
				+ " , treeName VARCHAR(255) NOT NULL " + " , maxChildren INT NULL " + " , maxItems INT NULL " + " , treeType VARCHAR(255) NOT NULL "
				+ " , N INT NULL "  + " , minimums TEXT NULL "  + " , maximums TEXT NULL " 
				+ ")";
		logger.log("create table: \n" + sql, "query", LogLevel.DEBUG, true);

		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);

			// create data table if it doesn't exist
			// all trees in one table 'rtree_data'

			stmt = conn.createStatement();

			sql = "CREATE TABLE IF NOT EXISTS " + tablePrefix + "_data (nodeId VARCHAR(255) NOT NULL, "
					+ " parent VARCHAR(255) NULL, " + " rectangle TEXT NULL, " + " items TEXT NULL, "
					+ " children TEXT NULL, " + " PRIMARY KEY ( nodeId ))";
			logger.log("[QUERY] MySQL create table: \n" + sql);

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
					+ " treeType VARCHAR(255) NOT NULL, "
					+ " properties TEXT NULL, " 
					+ " PRIMARY KEY ( id ) "
					+ ")";
			
			logger.log("[QUERY] MySQL create table: \n" + sql);
			
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);

		} catch (SQLException e) {
			logger.log(e);
			e.printStackTrace();
		}

	}
	

}
