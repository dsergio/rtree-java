package co.dsergio.rtree.business.dto;

import java.util.List;

import org.json.simple.JSONObject;

public class RectangleDouble {
	
	public List<Double> dimensionArray1;
	public List<Double> dimensionArray2;
	public int numberDimensions;
	public JSONObject json;
	
	public RectangleDouble() {
	}
	
	public RectangleDouble(List<Double> dimensionArray1, List<Double> dimensionArray2, int numberDimensions) {
		this.dimensionArray1 = dimensionArray1;
		this.dimensionArray2 = dimensionArray2;
		this.numberDimensions = numberDimensions;
	}

}
