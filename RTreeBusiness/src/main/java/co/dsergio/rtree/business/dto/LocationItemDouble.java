package co.dsergio.rtree.business.dto;

import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

public class LocationItemDouble {
	
	public List<Double> dimensionArray;
	public int numberDimensions;
	public String type;
	public String id;
	public Map<String, String> itemProperties;
	public JSONObject json;
	
	public LocationItemDouble() {
		
	}
	
	public LocationItemDouble(List<Double> dimensionArray, int numberDimensions, String type, String id, Map<String, String> itemProperties) {
		this.dimensionArray = dimensionArray;
		this.numberDimensions = numberDimensions;
		this.type = type;
		this.id = id;
		this.itemProperties = itemProperties;
	}
}
