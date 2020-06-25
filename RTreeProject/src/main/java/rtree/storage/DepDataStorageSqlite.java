package rtree.storage;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import rtree.log.ILogger;

/**
 * 
 * Sqlite RTree storage implementation
 * 
 * @author David Sergio
 *
 */
public class DepDataStorageSqlite extends DepDataStorageSQLBase {

	public DepDataStorageSqlite(ILogger logger, String treeName) {
		super(StorageType.SQLITE, logger, treeName);
		init();
	}

	@Override
	public void init() {

		try {
			Class.forName("org.sqlite.JDBC");
			String connectionString = "jdbc:sqlite:" + System.getProperty("user.dir") + java.io.File.separator + "rtree" + ".db";
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

		String sql = " CREATE TABLE IF NOT EXISTS rtree_metadata" + " (id INTEGER PRIMARY KEY AUTOINCREMENT "
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
