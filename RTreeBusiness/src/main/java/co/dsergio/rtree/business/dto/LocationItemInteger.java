package co.dsergio.rtree.business.dto;

import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

public class LocationItemInteger {
	
	public List<Integer> dimensionArray;
	public int numberDimensions;
	public String type;
	public String id;
	public Map<String, String> itemProperties;
	public JSONObject json;
	
	public LocationItemInteger() {
		
	}
	
	public LocationItemInteger(List<Integer> dimensionArray, int numberDimensions, String type, String id, Map<String, String> itemProperties) {
		this.dimensionArray = dimensionArray;
		this.numberDimensions = numberDimensions;
		this.type = type;
		this.id = id;
		this.itemProperties = itemProperties;
	}
}
