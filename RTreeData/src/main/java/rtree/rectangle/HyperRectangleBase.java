package rtree.rectangle;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rtree.item.ILocationItem;
import rtree.item.IRType;

/**
 * 
 * An abstract cuboid is defined by the space contained by two n-dimensional points.
 * For example, a line with two endpoints defines the space contained by two 1-dimensional points.
 * A Rectangle defines the space contained by two 2-dimensional points. 
 * A RectangularCuboid defines the space by two 3-dimensional points.
 * 
 * @param <T> {@link rtree.item.IRType} - the type of the dimensions
 *
 */
public abstract class HyperRectangleBase<T extends IRType<T>> implements IHyperRectangle<T> {
	
	protected List<T> dimensionArray1;
	protected List<T> dimensionArray2;
	protected int numberDimensions;
	protected int level;
	
	public HyperRectangleBase(int numberDimensions) {
		this.numberDimensions = numberDimensions;
		dimensionArray1 = new ArrayList<T>();
		dimensionArray2 = new ArrayList<T>();
		while (dimensionArray1.size() < numberDimensions) {
			dimensionArray1.add(null);
			dimensionArray2.add(null);
		}
		
	}
	
	public List<T> getDimensionArray1() {
		return dimensionArray1;
	}
	
	public List<T> getDimensionArray2() {
		return dimensionArray2;
	}
	
	public int getNumberDimensions() {
		return numberDimensions;
	}

	public void setDim1(int dim, T value) {
		if (dim < 0 || dim >= numberDimensions) {
			throw new IllegalArgumentException("setDim1 - you entered dim: " + dim + " value: " + value + ", but min dimension 0, max dimension " + numberDimensions);
		}
		dimensionArray1.set(dim, value);
	}
	
	public void setDim2(int dim, T value) {
		if (dim < 0 || dim >= numberDimensions) {
			throw new IllegalArgumentException("setDim2 - you entered dim: " + dim + " value: " + value + ", but min dimension 0, max dimension " + numberDimensions);
		}
		dimensionArray2.set(dim, value);
	}
	
	public T getDim1(int dim) {
		if (dim < 0 || dim >= numberDimensions) {
			throw new IllegalArgumentException("getDim1 - you entered dim: " + dim + ", but min dimension 0, max dimension " + numberDimensions);
		}
		return dimensionArray1.get(dim);
	}
	
	public T getDim2(int dim) {
		if (dim < 0 || dim >= numberDimensions) {
			throw new IllegalArgumentException("getDim2 - you entered dim: " + dim + ", but min dimension 0, max dimension " + numberDimensions);
		}
		return dimensionArray2.get(dim);
	}
	
	public boolean containsPoint(ILocationItem<T> item) {
		
		if (item.getNumberDimensions() != numberDimensions) {
			throw new IllegalArgumentException("Item " + item + " has incorrect dimension value. item.getNumberDimensions(): " + item.getNumberDimensions() + "  numberDimensions: " + numberDimensions);
		}
		boolean ret = true;
		for (int i = 0; i < numberDimensions; i++) {
			if (item.getDim(i).compareTo(dimensionArray1.get(i)) < 0 || item.getDim(i).compareTo(dimensionArray2.get(i)) > 0) {
				ret = false;
			}
		}
		return ret;
	}
	
	public double getSpace() {
		
		double spaceTotal = 1; 
		for (int i = 0; i < dimensionArray1.size(); i++) {
			if (dimensionArray1.get(i) == null || dimensionArray1.get(i) == null) {
				throw new IllegalArgumentException("Hyperrectangle is missing data.");
			}
			spaceTotal = spaceTotal * Math.abs((getDim1(i).distanceTo(getDim2(i))));
		}
		
//		return spaceTotal;
		return round(spaceTotal);
	}
	
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	public static <T extends IRType<T>> IHyperRectangle<T> sumRectanglesNDimensional(IHyperRectangle<T> rectangle, ILocationItem<T> item) {
		if (rectangle == null) {
			return null;
		}
		if (item == null) {
			return rectangle;
		}
		
		if (rectangle.getNumberDimensions() != item.getNumberDimensions()) {
			throw new IllegalArgumentException("Rectangle and item dimensions must be the same.");
		}
		
		List<T> min = new ArrayList<T>();
		List<T> max = new ArrayList<T>();
		
		for (int i = 0; i < item.getNumberDimensions(); i++) {
			if (item.getDim(i).compareTo(rectangle.getDim1(i)) < 0) {
				min.add(item.getDim(i));
			} else {
				min.add(rectangle.getDim1(i));
			}
			if (item.getDim(i).compareTo(rectangle.getDim2(i)) > 0) {
				max.add(item.getDim(i));
			} else {
				max.add(rectangle.getDim2(i));
			}
		}
		
		IHyperRectangle<T> newRect = new HyperRectangle<T>(item.getNumberDimensions());
		for (int i = 0; i < item.getNumberDimensions(); i++) {
			newRect.setDim1(i, min.get(i));
			newRect.setDim2(i, max.get(i));
		}
		
		return newRect;
		
	}
	
	
	public static <T extends IRType<T>> boolean rectanglesOverlap(IHyperRectangle<T> r1, IHyperRectangle<T> r2) {
		
		if (r1.getNumberDimensions() != r2.getNumberDimensions()) {
			throw new IllegalArgumentException("r1 and r2 must have equal number of dimensions.");
		}
		
		for (int i = 0; i < r1.getNumberDimensions(); i++) {
			if (r2.getDim1(i).compareTo(r1.getDim2(i)) > 0) {
				return false;
			}
			if (r2.getDim2(i).compareTo(r1.getDim1(i)) < 0) {
				return false;
			}
		}
		
		return true;
	}
	
	public static <T extends IRType<T>> IHyperRectangle<T> twoPointsRectangles(ILocationItem<T> item1, ILocationItem<T> item2) {
		if (item1 == null || item2 == null) {
			return null;
		}
		
		List<T> min = new ArrayList<T>();
		List<T> max = new ArrayList<T>();
		
		for (int i = 0; i < item1.getNumberDimensions(); i++) {
			if (item1.getDim(i).compareTo(item2.getDim(i)) < 0) {
				min.add(item1.getDim(i));
			} else {
				min.add(item2.getDim(i));
			}
			if (item1.getDim(i).compareTo(item2.getDim(i)) > 0) {
				max.add(item1.getDim(i));
			} else {
				max.add(item2.getDim(i));
			}
		}
		
		IHyperRectangle<T> newRect = new HyperRectangle<T>(item1.getNumberDimensions());
		for (int i = 0; i < item1.getNumberDimensions(); i++) {
			newRect.setDim1(i, min.get(i));
			newRect.setDim2(i, max.get(i));
		}
		
		return newRect;
		
	}
	
	public static <T extends IRType<T>> double areaRectangles(IHyperRectangle<T> r1, IHyperRectangle<T> r2) {
		
		List<T> min = new ArrayList<T>();
		List<T> max = new ArrayList<T>();
		
		for (int i = 0; i < r1.getNumberDimensions(); i++) {
			if (r1.getDim1(i).compareTo(r2.getDim1(i)) < 0) {
				min.add(r1.getDim1(i));
			} else {
				min.add(r2.getDim1(i));
			}
			if (r1.getDim1(i).compareTo(r2.getDim1(i)) > 0) {
				max.add(r1.getDim1(i));
			} else {
				max.add(r2.getDim1(i));
			}
		}
		
		double area = 1;
		for (int i = 0; i < r1.getNumberDimensions(); i++) {
			area *= Math.abs(max.get(i).distanceTo(min.get(i)));
		}
		
		return round(area);
	}
	
	public static <T extends IRType<T>> IHyperRectangle<T> sumRectangles(List<IHyperRectangle<T>> rectangles) {
		if (rectangles == null || rectangles.size() == 0) {
			return null;
		}
		if (rectangles.size() > 1) {
			
			List<T> min = new ArrayList<T>();
			List<T> max = new ArrayList<T>();
			
			IHyperRectangle<T> rect = rectangles.get(0);
			for (int i = 0; i < rect.getNumberDimensions(); i++) {
				min.add(rect.getDim1(i));
				max.add(rect.getDim2(i));
			}
			
			for (IHyperRectangle<T> r : rectangles) {
				for (int i = 0; i < rect.getNumberDimensions(); i++) {
					
					if (r.getDim1(i).compareTo(min.get(i)) < 0) {
						min.set(i, r.getDim1(i));
					}
					
					if (r.getDim2(i).compareTo(max.get(i)) > 0) {
						max.set(i, r.getDim2(i));
					}
					
				}
			}
			
			IHyperRectangle<T> newRect = new HyperRectangle<T>(rect.getNumberDimensions());
			for (int i = 0; i < rect.getNumberDimensions(); i++) {
				newRect.setDim1(i, min.get(i));
				newRect.setDim2(i, max.get(i));
			}
			
			return newRect;
						
		} else {
			return rectangles.get(0);
		}
	}
	
	private static double round(double value) {
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(3, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}
}
