package cloudrtree;

import java.util.ArrayList;
import java.util.List;

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
public abstract class CuboidBase {
	
	protected List<Integer> dimensionArray1;
	protected List<Integer> dimensionArray2;
	protected int numberDimensions;
	
	public CuboidBase(int numberDimensions) {
		this.numberDimensions = numberDimensions;
		dimensionArray1 = new ArrayList<Integer>();
		dimensionArray2 = new ArrayList<Integer>();
		while (dimensionArray1.size() < numberDimensions) {
			dimensionArray1.add(null);
			dimensionArray2.add(null);
		}
	}
	
	/**
	 * 
	 * @param dim Dimension. 0 for "x", 1 for "y", 2 for "z", etc.
	 * @param value
	 */
	public void setDim1(int dim, int value) {
		if (dim < 0 || dim >= numberDimensions) {
			throw new IllegalArgumentException("min dimension 0, max dimension " + numberDimensions);
		}
		dimensionArray1.set(dim, value);
	}
	
	/**
	 * 
	 * @param dim Dimension. 0 for "x", 1 for "y", 2 for "z", etc.
	 * @param value
	 */
	public void setDim2(int dim, int value) {
		if (dim < 0 || dim >= numberDimensions) {
			throw new IllegalArgumentException("min dimension 0, max dimension " + numberDimensions);
		}
		dimensionArray2.set(dim, value);
	}
	
	/**
	 * 
	 * @param dim Dimension. 0 for "x", 1 for "y", 2 for "z", etc.
	 * @return
	 */
	public Integer getDim1(int dim) {
		if (dim < 0 || dim >= numberDimensions) {
			throw new IllegalArgumentException("min dimension 0, max dimension " + numberDimensions);
		}
		return dimensionArray1.get(dim);
	}
	
	/**
	 * 
	 * @param dim Dimension. 0 for "x", 1 for "y", 2 for "z", etc.
	 * @return
	 */
	public Integer getDim2(int dim) {
		if (dim < 0 || dim >= numberDimensions) {
			throw new IllegalArgumentException("min dimension 0, max dimension " + numberDimensions);
		}
		return dimensionArray2.get(dim);
	}
}
