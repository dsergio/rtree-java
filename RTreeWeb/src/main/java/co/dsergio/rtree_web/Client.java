package co.dsergio.rtree_web;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Client {
	
	private String apiUrl;
	
	public Client() {
		
		Configurations configs = new Configurations();
		try {

			if (System.getenv("RTreeApiUrl") != null) {
				apiUrl = System.getenv("RTreeApiUrl");
			} else {
				Configuration config = configs.properties(new File("resources/config.properties"));
				apiUrl = config.getString("ApiUrl");
			}

		} catch (ConfigurationException cex) {
			cex.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public JSONArray getArray(String resource) {
		
		try {
			
			URL urlObj = new URL(apiUrl + "/" + resource);
			URLConnection urlCon = urlObj.openConnection();
			urlCon.setRequestProperty("Content-Type", "application/json");
			InputStream inputStream = urlCon.getInputStream();
			BufferedInputStream reader = new BufferedInputStream(inputStream);
			
			byte[] contents = new byte[1024];
			int bytesRead = 0;
			String jsonStr = "";
			while((bytesRead = reader.read(contents)) != -1) { 
				jsonStr += new String(contents, 0, bytesRead);              
			}
			
			JSONParser parser = new JSONParser();
			
			
			JSONArray arr = (JSONArray) parser.parse(jsonStr);
			
			
			JSONArray retArr = new JSONArray();
			for (int i = 0; i < arr.size(); i++) {
				JSONObject each = (JSONObject) parser.parse((String) arr.get(i));
				retArr.add(each);
			}
			
			return retArr;
			
		} catch (Exception e) {
			System.out.println("*******-------****** Error: " + e.getMessage());
			e.printStackTrace();
		}
		
		return new JSONArray();
	}
	
	public JSONObject getObject(String resource) {
		
		try {
			
			URL urlObj = new URL(apiUrl + "/" + resource);
			URLConnection urlCon = urlObj.openConnection();
			urlCon.setRequestProperty("Content-Type", "application/json");
			InputStream inputStream = urlCon.getInputStream();
			BufferedInputStream reader = new BufferedInputStream(inputStream);
			
			byte[] contents = new byte[1024];
			int bytesRead = 0;
			String jsonStr = "";
			while((bytesRead = reader.read(contents)) != -1) { 
				jsonStr += new String(contents, 0, bytesRead);              
			}
			
			JSONParser parser = new JSONParser();
			
			
			JSONObject obj = (JSONObject) parser.parse(jsonStr);
			
			return obj;
			
		} catch (Exception e) {
			System.out.println("*******-------****** Error: " + e.getMessage());
			e.printStackTrace();
		}
		
		return new JSONObject();
	}
}
