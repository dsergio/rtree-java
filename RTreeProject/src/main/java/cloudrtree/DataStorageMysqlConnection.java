package cloudrtree;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * MySQL Connection Implementation
 * 
 * @author David Sergio
 *
 */
public class DataStorageMysqlConnection {

	private Connection conn;
	private ILogger logger;

	public DataStorageMysqlConnection(String creds, ILogger logger) throws Exception {
		this.logger = logger;
		getConnection(creds);
	}

	public void close() {
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean insert(String nodeId, String children, String parent, String items, String rectangle,
			String tableName) {
		
		String query = "INSERT INTO `rtree_data` (`nodeId`, `parent`, `rectangle`, `items`, `children`) "
				+ "VALUES (?, ?, ?, ?, ?);";

		PreparedStatement stmt = null;
		int c = 1;

		try {

			stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

			stmt.setString(c++, nodeId);
			stmt.setString(c++, parent);
			stmt.setString(c++, rectangle);
			stmt.setString(c++, items);
			stmt.setString(c++, children);
			
			logger.log("[MYSQL]: " + stmt.toString().replace("com.mysql.cj.jdbc.ClientPreparedStatement: ", ""));

			stmt.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}

	public void update(String tableName, String nodeId, String children, String parent, String items,
			String rectangle) {
		
		String update = "UPDATE `rtree_data` ";
		
		String set = " SET nodeId = nodeId ";

		if (children != null) {
			set += ", `children` = ? ";
		}
		if (parent != null) {
			set += ", `parent` = ? ";
		}
		if (items != null) {
			set += ", `items` = ? ";
		}
		if (rectangle != null) {
			set += ", `rectangle` = ? ";
		}

		String where = " WHERE `nodeId` = ?";

		String query = update + set + where;
//		logger.log("QUERY: " + query);

		int c = 1;

		try {

			PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

			if (children != null) {
				stmt.setString(c++, children);
			}
			if (parent != null) {
				stmt.setString(c++, parent);
			}
			if (items != null) {
				stmt.setString(c++, items);
			}
			if (rectangle != null) {
				stmt.setString(c++, rectangle);
			}
			stmt.setString(c++, nodeId);
			
			logger.log("[MYSQL]: " + stmt.toString().replace("com.mysql.cj.jdbc.ClientPreparedStatement: ", ""));

			stmt.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public RTreeNode select(String tableName, String nodeId, RTreeCache cache) {
		
		String select = " SELECT * FROM rtree_data ";
		
		String where = " WHERE `nodeId` = ?";

		String query = select + where;

		
		
		RTreeNode returnNode = null;

		PreparedStatement stmt;
		try {
			stmt = conn.prepareStatement(query);
			
			stmt.setString(1, nodeId);
			
			logger.log("[MYSQL]: " + stmt.toString().replace("com.mysql.cj.jdbc.ClientPreparedStatement: ", ""));
			
			ResultSet resultSet = stmt.executeQuery();

			String children = null;
			String parent = null;
			String rectangle = null;
			String items = null;

			Rectangle r = new Rectangle();
			if (resultSet.next()) {
				children = resultSet.getString("children");
				parent = resultSet.getString("parent");
				rectangle = resultSet.getString("rectangle");
				items = resultSet.getString("items");
				
				r = new Rectangle(rectangle);

				returnNode = new RTreeNode(nodeId, children, parent, cache, logger);
				returnNode.setRectangle(r);
				returnNode.setItemsJson(items);
				
				logger.log("select: nodeId: " + nodeId);
				logger.log("select: node children: " + returnNode.children);
				logger.log("select: node rectangle: " + r.toString());
				logger.log("select: node items: " + returnNode.getItemsJSON().toJSONString());
				logger.log("select: items: " + items);
				logger.log("select: parent: " + returnNode.parent);
			} else {

				logger.log("select: nodeId: " + nodeId);
				logger.log("select: node children: ");
				logger.log("select: node rectangle: ");
				logger.log("select: node items: ");
				logger.log("select: items: ");
				logger.log("select: parent: ");
			}
			
			return returnNode;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	public void initializeDb(String treeName) throws Exception {

		if (!treeName.matches("^[a-zA-Z0-9_]+$")) {
			logger.log("Illegal table name");
			throw new IllegalArgumentException(treeName);
		}
		
		// create metadata table if it doesn't exist
		// metadata table contains the maxChildren and maxItems parameters, 
		// and the min and max spatial boundaries

		Statement stmt = null;

		String sql = " CREATE TABLE IF NOT EXISTS rtree_metadata"
				+ " (id INT PRIMARY KEY AUTO_INCREMENT "
				+ " , treeName VARCHAR(255) NOT NULL " 
				+ " , maxChildren INT NULL " 
				+ " , maxItems INT NULL "
				+ " , minX INT NULL "
				+ " , maxX INT NULL "
				+ " , minY INT NULL "
				+ " , maxY INT NULL "
				+ ")";
		logger.log("create table: \n" + sql);

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

	}

	private void getConnection(String creds) throws Exception {
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
		    
		}
		catch (ConfigurationException cex)
		{
		    throw cex;
		}

		// use a configuration file, not a text file
		
//		try {
//			File myObj = new File(creds);
//			Scanner myReader = new Scanner(myObj);
//
//			while (myReader.hasNextLine()) {
//
//				String data = myReader.nextLine();
//				if (data.split("=").length < 2) {
//					throw new Exception("MySQL creds.txt Invalid Format");
//				}
//				if (data.split("=")[0].equals("host")) {
//					endpoint = data.split("=")[1];
//				} else if (data.split("=")[0].equals("user")) {
//					username = data.split("=")[1];
//				} else if (data.split("=")[0].equals("password")) {
//					password = data.split("=")[1];
//				} else if (data.split("=")[0].equals("database")) {
//					database = data.split("=")[1];
//				}
////				logger.log(data);
//			}
//
//			myReader.close();
//		} catch (FileNotFoundException e) {
//			logger.log("Credentials File is missing.");
//			throw e;
//		}

		String url = "jdbc:mysql://" + host + ":3306/" + database;

		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			logger.log("Failed to open MYSQL connection. " + e.getMessage());
			throw e;
		}

		this.conn = conn;
	}

	public Connection getConn() {
		return conn;
	}

}
