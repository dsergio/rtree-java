package co.dsergio.rtree.business.dto;

import java.util.List;

import rtree.item.ILocationItem;
import rtree.item.RDouble;
import rtree.item.RInteger;
import rtree.rectangle.IHyperRectangle;

public class RTreeInteger {
	
	public RTreeInteger() {
		
	}
	
	public RTreeInteger(String name, int numDimensions, List<RectangleInteger> rectangles, List<LocationItemInteger> points) {
		this.name = name;
		this.numDimensions = numDimensions;
		this.rectangles = rectangles;
		this.points = points;
	}
	
	public String name;
	public int numDimensions;
	public List<RectangleInteger> rectangles;
	public List<LocationItemInteger> points;

}
