package rtree.item;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * This class is like a space calculation but the difference is that lower dimensions are possible
 * For example, a 3D tree might contain points that are all on a plane, so that
 * the 'space' is 2D and positive even if the simple 'space' calculation returns a 'volume' of 0.
 * 
 */
public class BoundingBox {
	
	private final int numberDimensions;
	
	/**
	 * List of dimensions, each of type Double. 
	 * Each dimension is the size of that dimension in the bounding box.
	 * The size of this list is equal to the number of dimensions specified at construction.
	 */
	protected List<Double> dimensionArray;
	
	/**
	 * Constructs a BoundingBox with the specified number of dimensions.
	 * 
	 * @param numberDimensions the number of dimensions for this bounding box
	 * @throws IllegalArgumentException if numberDimensions is less than 1
	 */
	public BoundingBox(int numberDimensions) throws IllegalArgumentException {
		if (numberDimensions < 1) {
			throw new IllegalArgumentException("BoundingBox minimum dimension is 1.");
		}
		this.numberDimensions = numberDimensions;
		dimensionArray = new ArrayList<Double>();
		while (dimensionArray.size() < numberDimensions) {
			dimensionArray.add(null);
		}
	}
	
	/**
	 * Get the array of dimensions.
	 * @return a list of dimensions, each of type Double
	 */
	public List<Double> getDimensionArray() {
		return dimensionArray;
	}
	
	/**
	 * Get the number of dimensions of the bounding box.
	 * @return the number of dimensions
	 */
	public int getNumberDimensions() {
		return numberDimensions;
	}
	
	/**
	 * Get the number of dimensions that are not null and not zero.
	 * @return the count of dimensions that are not null and not zero
	 */
	public int getBoxDimensions() {
		int c = 0;
		for (int i = 0; i < dimensionArray.size(); i++) {
			if (dimensionArray.get(i) != null) {
				if (dimensionArray.get(i) != 0) {
					c++;
				}
			}
		}
		return c;
	}
	
	/**
	 * Calculate the 'space' of the bounding box.
	 * This is the product of all dimensions that are not null and not zero.
	 * If all dimensions are null or zero, it returns 0.
	 * 
	 * @return the calculated space of the bounding box
	 */
	public double getBoxSpace() {
		double space = 1;
		double c = 0;
		for (int i = 0; i < dimensionArray.size(); i++) {
			if (dimensionArray.get(i) != null) {
				if (dimensionArray.get(i) != 0) {
					space *= dimensionArray.get(i);
				} else {
					c++;
				}
			}
		}
		if (c == dimensionArray.size()) {
			return 0;
		} else {
			return space;
		}
	}
	
	/**
	 * Set the value for a specific dimension.
	 * 
	 * @param dim Dimension. 0 for "x", 1 for "y", 2 for "z", etc.
	 * @param value value to set for the dimension
	 * @throws IllegalArgumentException if dim is out of bounds
	 */
	public void setDim(int dim, Double value) throws IllegalArgumentException {
		if (dim < 0 || dim >= numberDimensions) {
			throw new IllegalArgumentException("min dimension 0, max dimension " + numberDimensions + " you entered dim: " + dim + " value: " + value);
		}
		dimensionArray.set(dim, value);
	}
	
	/**
	 * Get the value for a specific dimension.
	 * 
	 * @param dim Dimension. 0 for "x", 1 for "y", 2 for "z", etc.
	 * @return value for the dimension
	 * @throws IllegalArgumentException if dim is out of bounds
	 */
	public Double getDim(int dim) throws IllegalArgumentException {
		if (dim < 0 || dim >= numberDimensions) {
			throw new IllegalArgumentException("min dimension 0, max dimension " + numberDimensions + " you entered dim: " + dim);
		}
		return dimensionArray.get(dim);
	}
	
}
