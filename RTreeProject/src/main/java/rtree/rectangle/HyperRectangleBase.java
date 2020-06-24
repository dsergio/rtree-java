package rtree.rectangle;

import java.util.ArrayList;
import java.util.List;

import rtree.item.ILocationItem;

/**
 * An abstract cuboid class. 
 * An abstract cuboid is defined by the space contained by two n-dimensional points.
 * For example, a line with two endpoints defines the space contained by two 1-dimensional points.
 * A Rectangle defines the space contained by two 2-dimensional points. 
 * A RectangularCuboid defines the space by two 3-dimensional points.
 * 
 * @author David Sergio
 *
 */
public abstract class HyperRectangleBase implements IHyperRectangle {
	
	protected List<Integer> dimensionArray1;
	protected List<Integer> dimensionArray2;
	protected int numberDimensions;
	protected int level;
	
	public HyperRectangleBase(int numberDimensions) {
		this.numberDimensions = numberDimensions;
		dimensionArray1 = new ArrayList<Integer>();
		dimensionArray2 = new ArrayList<Integer>();
		while (dimensionArray1.size() < numberDimensions) {
			dimensionArray1.add(null);
			dimensionArray2.add(null);
		}
		
	}
	
	public List<Integer> getDimensionArray1() {
		return dimensionArray1;
	}
	
	public List<Integer> getDimensionArray2() {
		return dimensionArray2;
	}
	
	public int getNumberDimensions() {
		return numberDimensions;
	}

	public void setDim1(int dim, Integer value) {
		if (dim < 0 || dim >= numberDimensions) {
			throw new IllegalArgumentException("setDim1 - you entered dim: " + dim + " value: " + value + ", but min dimension 0, max dimension " + numberDimensions);
		}
		dimensionArray1.set(dim, value);
	}
	
	public void setDim2(int dim, Integer value) {
		if (dim < 0 || dim >= numberDimensions) {
			throw new IllegalArgumentException("setDim2 - you entered dim: " + dim + " value: " + value + ", but min dimension 0, max dimension " + numberDimensions);
		}
		dimensionArray2.set(dim, value);
	}
	
	public Integer getDim1(int dim) {
		if (dim < 0 || dim >= numberDimensions) {
			throw new IllegalArgumentException("getDim1 - you entered dim: " + dim + ", but min dimension 0, max dimension " + numberDimensions);
		}
		return dimensionArray1.get(dim);
	}
	
	public Integer getDim2(int dim) {
		if (dim < 0 || dim >= numberDimensions) {
			throw new IllegalArgumentException("getDim2 - you entered dim: " + dim + ", but min dimension 0, max dimension " + numberDimensions);
		}
		return dimensionArray2.get(dim);
	}
	
	public boolean containsPoint(ILocationItem item) {
		
		if (item.getNumberDimensions() != numberDimensions) {
			throw new IllegalArgumentException("Item " + item + " has incorrect dimension value. item.getNumberDimensions(): " + item.getNumberDimensions() + "  numberDimensions: " + numberDimensions);
		}
		boolean ret = true;
		for (int i = 0; i < numberDimensions; i++) {
			if (item.getDim(i) < dimensionArray1.get(i) || item.getDim(i) > dimensionArray2.get(i)) {
				ret = false;
			}
		}
		return ret;
	}
	
	public int getSpace() {
		
		int spaceTotal = 1; 
		for (int i = 0; i < dimensionArray1.size(); i++) {
			if (dimensionArray1.get(i) == null || dimensionArray1.get(i) == null) {
				throw new IllegalArgumentException("Hyperrectangle is missing data.");
			}
			spaceTotal = spaceTotal * Math.abs((getDim1(i) - getDim2(i)));
		}
		
		return spaceTotal;
	}
	
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	public static IHyperRectangle sumRectanglesNDimensional(IHyperRectangle rectangle, ILocationItem item) {
		if (rectangle == null) {
			return null;
		}
		if (item == null) {
			return rectangle;
		}
		
		if (rectangle.getNumberDimensions() != item.getNumberDimensions()) {
			throw new IllegalArgumentException("Rectangle and item dimensions must be the same.");
		}
		
		List<Integer> min = new ArrayList<Integer>();
		List<Integer> max = new ArrayList<Integer>();
		
		for (int i = 0; i < item.getNumberDimensions(); i++) {
			min.add(Math.min(item.getDim(i), rectangle.getDim1(i)));
			max.add(Math.max(item.getDim(i), rectangle.getDim2(i)));
		}
		
		IHyperRectangle newRect = new HyperCuboid(item.getNumberDimensions());
		for (int i = 0; i < item.getNumberDimensions(); i++) {
			newRect.setDim1(i, min.get(i));
			newRect.setDim2(i, max.get(i));
		}
		
		return newRect;
		
	}
	
	
	public static boolean rectanglesOverlapNDimensional(IHyperRectangle r1, IHyperRectangle r2) {
		
		if (r1.getNumberDimensions() != r2.getNumberDimensions()) {
			throw new IllegalArgumentException("r1 and r2 must have equal number of dimensions.");
		}
		
		for (int i = 0; i < r1.getNumberDimensions(); i++) {
			if (r2.getDim1(i) > r1.getDim2(i)) {
				return false;
			}
			if (r2.getDim2(i) < r1.getDim1(i)) {
				return false;
			}
		}
		
		return true;
	}
	
	public static IHyperRectangle twoPointsRectanglesNDimensional(ILocationItem item1, ILocationItem item2) {
		if (item1 == null || item2 == null) {
			return null;
		}
		
		List<Integer> min = new ArrayList<Integer>();
		List<Integer> max = new ArrayList<Integer>();
		
		for (int i = 0; i < item1.getNumberDimensions(); i++) {
			min.add(Math.min(item1.getDim(i), item2.getDim(i)));
			max.add(Math.max(item1.getDim(i), item2.getDim(i)));
		}
		
		IHyperRectangle newRect = new HyperCuboid(item1.getNumberDimensions());
		for (int i = 0; i < item1.getNumberDimensions(); i++) {
			newRect.setDim1(i, min.get(i));
			newRect.setDim2(i, max.get(i));
		}
		
		return newRect;
		
	}
	
	public static int areaRectanglesNDimensional(IHyperRectangle r1, IHyperRectangle r2) {
		
		List<Integer> min = new ArrayList<Integer>();
		List<Integer> max = new ArrayList<Integer>();
		
		for (int i = 0; i < r1.getNumberDimensions(); i++) {
			min.add(Math.min(r1.getDim1(i), r2.getDim1(i)));
			max.add(Math.max(r1.getDim2(i), r2.getDim2(i)));
		}
		
		int area = 1;
		for (int i = 0; i < r1.getNumberDimensions(); i++) {
			area *= Math.abs(max.get(i) - min.get(i));
		}
		return area;
	}
	
	public static IHyperRectangle sumRectanglesNDimensional(List<IHyperRectangle> rectangles) {
		if (rectangles == null || rectangles.size() == 0) {
			return null;
		}
		if (rectangles.size() > 1) {
			
			List<Integer> min = new ArrayList<Integer>();
			List<Integer> max = new ArrayList<Integer>();
			
			IHyperRectangle rect = rectangles.get(0);
			for (int i = 0; i < rect.getNumberDimensions(); i++) {
				min.add(Math.min(rect.getDim1(i), rect.getDim1(i)));
				max.add(Math.max(rect.getDim2(i), rect.getDim2(i)));
			}
			
			for (IHyperRectangle r : rectangles) {
				for (int i = 0; i < rect.getNumberDimensions(); i++) {
					min.set(i, Math.min(r.getDim1(i), min.get(i)));
					max.set(i, Math.max(r.getDim2(i), max.get(i)));
				}
			}
			
			IHyperRectangle newRect = new HyperCuboid(rect.getNumberDimensions());
			for (int i = 0; i < rect.getNumberDimensions(); i++) {
				newRect.setDim1(i, min.get(i));
				newRect.setDim2(i, max.get(i));
			}
			
			return newRect;
						
		} else {
			return rectangles.get(0);
		}
	}
}
