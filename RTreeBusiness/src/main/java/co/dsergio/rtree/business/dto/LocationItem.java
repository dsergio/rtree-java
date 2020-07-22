package co.dsergio.rtree.business.dto;

import java.util.List;


public class LocationItem {
	
	public List<Integer> dimensionArray;
	public int numberDimensions;
	public String itemType;
	public String itemId;
	
	public LocationItem() {
		
	}
	
	public LocationItem(List<Integer> dimensionArray, int numberDimensions, String itemType, String itemId) {
		this.dimensionArray = dimensionArray;
		this.numberDimensions = numberDimensions;
		this.itemType = itemType;
		this.itemId = itemId;
	}
}
