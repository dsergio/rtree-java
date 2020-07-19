package co.dsergio.rtree.business.dto;

import java.util.List;

import rtree.item.ILocationItem;
import rtree.rectangle.IHyperRectangle;

public class RTree {
	
	public RTree(String name, int numDimensions, List<IHyperRectangle> rectangles, List<ILocationItem> points) {
		this.name = name;
		this.numDimensions = numDimensions;
		this.rectangles = rectangles;
		this.points = points;
	}
	
	public String name;
	public int numDimensions;
	public List<IHyperRectangle> rectangles;
	public List<ILocationItem> points;

}
