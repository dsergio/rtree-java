package rtree.item;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * This class is like a space calculation but the difference is that lower dimensions are possible
 * For example, a 3D tree might contain points that are all on a plane, so that
 * the 'space' is 2D and positive even if the simple 'space' calculation returns a 'volume' of 0.
 * 
 * @author David Sergio
 *
 * @param <T>
 */
public class BoundingBox {
	
	private final int numberDimensions;
	protected List<Double> dimensionArray;
	
	public BoundingBox(int numberDimensions) {
		this.numberDimensions = numberDimensions;
		dimensionArray = new ArrayList<Double>();
		while (dimensionArray.size() < numberDimensions) {
			dimensionArray.add(null);
		}
	}
	
	public List<Double> getDimensionArray() {
		return dimensionArray;
	}
	
	public int getNumberDimensions() {
		return numberDimensions;
	}
	
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
	
	public void setDim(int dim, Double value) {
		if (dim < 0 || dim >= numberDimensions) {
			throw new IllegalArgumentException("min dimension 0, max dimension " + numberDimensions + " you entered dim: " + dim + " value: " + value);
		}
		dimensionArray.set(dim, value);
	}
	
	public Double getDim(int dim) {
		if (dim < 0 || dim >= numberDimensions) {
			throw new IllegalArgumentException("min dimension 0, max dimension " + numberDimensions + " you entered dim: " + dim);
		}
		return dimensionArray.get(dim);
	}
	
}
