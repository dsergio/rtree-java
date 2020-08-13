package co.dsergio.rtree.business.dto;

import java.util.List;

import org.json.simple.JSONObject;

public class RectangleInteger {
	
	public List<Integer> dimensionArray1;
	public List<Integer> dimensionArray2;
	public int numberDimensions;
	public JSONObject json;
	
	public RectangleInteger() {
	}
	
	public RectangleInteger(List<Integer> dimensionArray1, List<Integer> dimensionArray2, int numberDimensions) {
		this.dimensionArray1 = dimensionArray1;
		this.dimensionArray2 = dimensionArray2;
		this.numberDimensions = numberDimensions;
	}

}
