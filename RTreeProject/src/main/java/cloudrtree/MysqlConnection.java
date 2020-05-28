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
	
	public MysqlConnection(String creds) {
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
	
	public boolean insert(String nodeId, String children, String parent, String items, String rectangle, String tableName) {
		
		String query = "INSERT INTO `" + tableName + "` (`nodeId`, `parent`, `rectangle`, `items`, `children`) " + 
				"VALUES (?, ?, ?, ?, ?);";
		
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
		
		String update = "UPDATE `" + tableName  + "` ";
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
		System.out.println("QUERY: " + query);
		
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
		
		String select = " SELECT * FROM " + tableName;
		String where = " WHERE `nodeId` = ?";
		
		String query = select + where;
		
		System.out.println("QUERY: " + query);
		
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
			
			CloudRTreeNode node =  new CloudRTreeNode(nodeId, children, parent, cache);
			node.setRectangle(r);
			node.setItemsJson(items);
			
			System.out.println("select: node rectangle: " + r.toString());
			System.out.println("select: node items: " + node.getItemsJSON().toJSONString());
			System.out.println("select: items: " + items);
			
			
			return node;
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	public void createTable(String tableName) {
		
		try {
			
			DatabaseMetaData dbm = conn.getMetaData();
			ResultSet rs = dbm.getTables(null, null, tableName, null);
			if (rs.next()) {
				return;
			}
			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      
		String sql = "CREATE TABLE IF NOT EXISTS " + tableName + 
	                   " (nodeId VARCHAR(255) NOT NULL, " +
	                   " parent VARCHAR(255) NULL, " + 
	                   " rectangle TEXT NULL, " + 
	                   " items TEXT NULL, " + 
	                   " children TEXT NULL, " + 
	                   " PRIMARY KEY ( nodeId ))";
		System.out.println("create table: \n" + sql);
	      try {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void getConnection(String creds) {
		String username = "";
		String password = "";
		String endpoint = "";
		String database = "";
		
		
		try {
			File myObj = new File(creds);
			Scanner myReader = new Scanner(myObj);
			
			
			while (myReader.hasNextLine()) {
				
				String data = myReader.nextLine();
				if (data.split("=")[0].equals("host")) {
					endpoint = data.split("=")[1];
				} else if (data.split("=")[0].equals("user")) {
					username = data.split("=")[1];
				} else if (data.split("=")[0].equals("password")) {
					password = data.split("=")[1];
				} else if (data.split("=")[0].equals("database")) {
					database = data.split("=")[1];
				}
				System.out.println(data);
			}
			
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		
		String url = "jdbc:mysql://" + endpoint + ":3306/" + database;
		
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
		}
		
		this.conn = conn;
	}

	public Connection getConn() {
		return conn;
	}

	
}







