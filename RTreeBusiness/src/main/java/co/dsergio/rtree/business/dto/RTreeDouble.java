package co.dsergio.rtree.business.dto;

import java.util.List;

public class RTreeDouble {
	
	public RTreeDouble() {
		
	}
	
	public RTreeDouble(String name, int numDimensions, List<RectangleDouble> rectangles, List<LocationItemDouble> points) {
		this.name = name;
		this.numDimensions = numDimensions;
		this.rectangles = rectangles;
		this.points = points;
	}
	
	public String name;
	public int numDimensions;
	public List<RectangleDouble> rectangles;
	public List<LocationItemDouble> points;

}
