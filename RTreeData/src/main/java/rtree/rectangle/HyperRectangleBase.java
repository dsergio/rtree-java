package rtree.rectangle;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

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
	
	/**
	 * the first dimension array
	 */
	protected List<T> dimensionArray1;
	
	/**
	 * the second dimension array
	 */
	protected List<T> dimensionArray2;
	
	/**
	 * the number of dimensions
	 */
	protected int numberDimensions;
	
	/**
	 * the level of the hyperrectangle in the R-tree
	 */
	protected int level;
	
	/**
	 * Constructs a HyperRectangleBase with the specified number of dimensions.
	 * 
	 * @param numberDimensions the number of dimensions for this hyperrectangle
	 * @throws IllegalArgumentException if numberDimensions is less than 1
	 */
	public HyperRectangleBase(int numberDimensions) throws IllegalArgumentException {
		this.numberDimensions = numberDimensions;
		
		if (numberDimensions < 1) {
			throw new IllegalArgumentException("HyperRectangleBase minimum dimension is 1.");
		}
		dimensionArray1 = new ArrayList<T>();
		dimensionArray2 = new ArrayList<T>();
		while (dimensionArray1.size() < numberDimensions) {
			dimensionArray1.add(null);
			dimensionArray2.add(null);
		}
		
	}
	
	@Override
	public List<T> getDimensionArray1() {
		return dimensionArray1;
	}
	
	@Override
	public List<T> getDimensionArray2() {
		return dimensionArray2;
	}
	
	@Override
	public int getNumberDimensions() {
		return numberDimensions;
	}
	
	@Override
	public void setDim1(int dim, T value) throws IllegalArgumentException {
		if (dim < 0 || dim >= numberDimensions) {
			throw new IllegalArgumentException("setDim1 - you entered dim: " + dim + " value: " + value + ", but min dimension 0, max dimension " + numberDimensions);
		}
		dimensionArray1.set(dim, value);
	}
	
	@Override
	public void setDim2(int dim, T value) throws IllegalArgumentException {
		if (dim < 0 || dim >= numberDimensions) {
			throw new IllegalArgumentException("setDim2 - you entered dim: " + dim + " value: " + value + ", but min dimension 0, max dimension " + numberDimensions);
		}
		dimensionArray2.set(dim, value);
	}
	
	@Override
	public T getDim1(int dim) throws IllegalArgumentException {
		if (dim < 0 || dim >= numberDimensions) {
			throw new IllegalArgumentException("getDim1 - you entered dim: " + dim + ", but min dimension 0, max dimension " + numberDimensions);
		}
		return dimensionArray1.get(dim);
	}
	
	@Override
	public T getDim2(int dim) throws IllegalArgumentException {
		if (dim < 0 || dim >= numberDimensions) {
			throw new IllegalArgumentException("getDim2 - you entered dim: " + dim + ", but min dimension 0, max dimension " + numberDimensions);
		}
		return dimensionArray2.get(dim);
	}
	
	@Override
	public boolean containsPoint(ILocationItem<T> item) throws IllegalArgumentException {
		
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
	
	@Override
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
	
	@Override
	public int getLevel() {
		return level;
	}
	
	@Override
	public void setLevel(int level) {
		this.level = level;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getJson() {
		
		JSONObject obj = new JSONObject();
		for (int i = 0; i < numberDimensions; i++) {
			
			switch (i) {
			case 0: 
				obj.put("x1", getDim1(i).getData().toString());
				obj.put("x2", getDim2(i).getData().toString());
				break;
			case 1:
				obj.put("y1", getDim1(i).getData().toString());
				obj.put("y2", getDim2(i).getData().toString());
				break;
			case 2:
				obj.put("z1", getDim1(i).getData().toString());
				obj.put("z2", getDim2(i).getData().toString());
				break;
			default:
				obj.put(i + "_1", getDim1(i).getData().toString());
				obj.put(i + "_2", getDim2(i).getData().toString());
				break;
			}
		}
		obj.put("level", level);
		return obj;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String toString() {
		
		JSONObject obj = getJson();
		obj.put("area", getSpace());
		
		return obj.toJSONString();
		
	}
	
	/**
	 * Sum a n-dimensional rectangle with an item.
	 * @param <T> the type of the dimensions, extending {@link rtree.item.IRType}
	 * @param rectangle the rectangle to sum with the item
	 * @param item the item to sum with the rectangle
	 * @return a new hyperrectangle that represents the sum of the rectangle and item
	 * @throws IllegalArgumentException if the rectangle and item dimensions do not match
	 */
	public static <T extends IRType<T>> IHyperRectangle<T> sumRectanglesNDimensional(IHyperRectangle<T> rectangle, ILocationItem<T> item) throws IllegalArgumentException {
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
	
	/**
	 * Check if two rectangles overlap in any dimension.
	 * @param <T> the type of the dimensions, extending {@link rtree.item.IRType}
	 * @param r1 the first rectangle
	 * @param r2 the second rectangle
	 * @return true if the rectangles overlap, false otherwise
	 * @throws IllegalArgumentException if the rectangles have different number of dimensions
	 */
	public static <T extends IRType<T>> boolean rectanglesOverlap(IHyperRectangle<T> r1, IHyperRectangle<T> r2) throws IllegalArgumentException {
		
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
	
	/**
	 * Create a hyperrectangle from two points. The points can be in any order, the
	 * method will create a rectangle that contains both points.
	 * 
	 * @param <T>   the type of the dimensions, extending {@link rtree.item.IRType}
	 * @param item1 the first location item
	 * @param item2 the second location item
	 * @return a new hyperrectangle that contains both points
	 * @throws IllegalArgumentException if the items have different number of
	 *                                  dimensions
	 */
	public static <T extends IRType<T>> IHyperRectangle<T> twoPointsRectangles(ILocationItem<T> item1, ILocationItem<T> item2) throws IllegalArgumentException {
		if (item1 == null || item2 == null) {
			return null;
		}
		
		if (item1.getNumberDimensions() != item2.getNumberDimensions()) {
			throw new IllegalArgumentException("item1 and item2 must have equal number of dimensions.");
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
	
	/**
	 * Calculate the area of the intersection of two rectangles.
	 * @param <T> the type of the dimensions, extending {@link rtree.item.IRType}
	 * @param r1 the first rectangle
	 * @param r2 the second rectangle
	 * @return the area of the intersection of the two rectangles
	 * @throws IllegalArgumentException if the rectangles have different number of dimensions
	 */
	public static <T extends IRType<T>> double areaRectangles(IHyperRectangle<T> r1, IHyperRectangle<T> r2) throws IllegalArgumentException {
		
		if (r1 == null || r2 == null) {
			return 0;
		}
		if (r1.getNumberDimensions() != r2.getNumberDimensions()) {
			throw new IllegalArgumentException("r1 and r2 must have equal number of dimensions.");
		}
		
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
	
	/**
	 * Sum a list of rectangles into a new rectangle that contains all the
	 * rectangles.
	 * 
	 * @param <T>        the type of the dimensions, extending
	 *                   {@link rtree.item.IRType}
	 * @param rectangles the list of rectangles to sum
	 * @return a new hyperrectangle that contains all the rectangles
	 * @throws IllegalArgumentException if the rectangles have different number of
	 *                                  dimensions
	 */
	public static <T extends IRType<T>> IHyperRectangle<T> sumRectangles(List<IHyperRectangle<T>> rectangles) throws IllegalArgumentException {
		if (rectangles == null || rectangles.size() == 0) {
			return null;
		}
		
		int rDim = rectangles.get(0).getNumberDimensions();
		if (rectangles.size() > 1) {
			
			List<T> min = new ArrayList<T>();
			List<T> max = new ArrayList<T>();
			
			IHyperRectangle<T> rect = rectangles.get(0);
			
			if (rect.getNumberDimensions() < 1) {
				throw new IllegalArgumentException("HyperRectangleBase minimum dimension is 1.");
			}
			if (rect.getNumberDimensions() != rDim) {
				throw new IllegalArgumentException("All rectangles must have the same number of dimensions.");
			}
			
			
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
	
	/**
	 * Round a double value to 3 decimal places.
	 * 
	 * @param value the value to round
	 * @return the rounded value
	 */
	private static double round(double value) {
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(3, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}
}
