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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MysqlConnection {

	private Connection conn;
	private ILogger logger;

	public MysqlConnection(String creds, ILogger logger) throws Exception {
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
		logger.log("QUERY: " + query);

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

			stmt.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public CloudRTreeNode select(String tableName, String nodeId, CloudRTreeCache cache) {
		
		String select = " SELECT * FROM rtree_data ";
		
		String where = " WHERE `nodeId` = ?";

		String query = select + where;

		logger.log("QUERY: " + query + " nodeId: " + nodeId);

		PreparedStatement stmt;
		try {
			stmt = conn.prepareStatement(query);

			stmt.setString(1, nodeId);
			ResultSet resultSet = stmt.executeQuery();

			String children = null;
			String parent = null;
			String rectangle = null;
			String items = null;

			if (resultSet.next()) {
				children = resultSet.getString("children");
				parent = resultSet.getString("parent");
				rectangle = resultSet.getString("rectangle");
				items = resultSet.getString("items");
			} else {
				return null;
			}

			Rectangle r = new Rectangle(rectangle);

			CloudRTreeNode node = new CloudRTreeNode(nodeId, children, parent, cache, logger);
			node.setRectangle(r);
			node.setItemsJson(items);

			logger.log("select: node rectangle: " + r.toString());
			logger.log("select: node items: " + node.getItemsJSON().toJSONString());
			logger.log("select: items: " + items);
			logger.log("select: parent: " + node.parent);

			return node;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	public void initializeDb(String treeName) throws Exception {

		if (!treeName.matches("^[a-zA-Z0-9_]*$")) {
			logger.log("Illegal table name");
			throw new IllegalArgumentException(treeName);
		}
		
		// create metadata table if it doesn't exist
		// metadata table contains the maxChildren and maxItems parameters

		Statement stmt = null;

		String sql = "CREATE TABLE IF NOT EXISTS rtree_metadata" + " (id INT PRIMARY KEY AUTO_INCREMENT, "
				+ " treeName VARCHAR(255) NOT NULL, " + " maxChildren INT NULL, " + " maxItems INT NULL) ";
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
		String endpoint = "";
		String database = "";

		try {
			File myObj = new File(creds);
			Scanner myReader = new Scanner(myObj);

			while (myReader.hasNextLine()) {

				String data = myReader.nextLine();
				if (data.split("=").length < 2) {
					throw new Exception("MySQL creds.txt Invalid Format");
				}
				if (data.split("=")[0].equals("host")) {
					endpoint = data.split("=")[1];
				} else if (data.split("=")[0].equals("user")) {
					username = data.split("=")[1];
				} else if (data.split("=")[0].equals("password")) {
					password = data.split("=")[1];
				} else if (data.split("=")[0].equals("database")) {
					database = data.split("=")[1];
				}
//				logger.log(data);
			}

			myReader.close();
		} catch (FileNotFoundException e) {
			logger.log("Credentials File is missing.");
			throw e;
		}

		String url = "jdbc:mysql://" + endpoint + ":3306/" + database;

		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			logger.log("SQLException: " + e.getMessage());
			throw e;
		}

		this.conn = conn;
	}

	public Connection getConn() {
		return conn;
	}

}
