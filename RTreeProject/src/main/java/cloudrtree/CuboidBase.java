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
public abstract class CuboidBase implements IHyperRectangle {
	
	protected List<Integer> dimensionArray1;
	protected List<Integer> dimensionArray2;
	protected int numberDimensions;
	protected int level;
	
	public CuboidBase(int numberDimensions) {
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
			throw new IllegalArgumentException("min dimension 0, max dimension " + numberDimensions);
		}
		dimensionArray1.set(dim, value);
	}
	
	public void setDim2(int dim, Integer value) {
		if (dim < 0 || dim >= numberDimensions) {
			throw new IllegalArgumentException("min dimension 0, max dimension " + numberDimensions);
		}
		dimensionArray2.set(dim, value);
	}
	
	public Integer getDim1(int dim) {
		if (dim < 0 || dim >= numberDimensions) {
			throw new IllegalArgumentException("min dimension 0, max dimension " + numberDimensions);
		}
		return dimensionArray1.get(dim);
	}
	
	public Integer getDim2(int dim) {
		if (dim < 0 || dim >= numberDimensions) {
			throw new IllegalArgumentException("min dimension 0, max dimension " + numberDimensions);
		}
		return dimensionArray2.get(dim);
	}
	
	public boolean containsPoint(ILocationItem item) {
		
		if (item.getNumberDimensions() != numberDimensions) {
			throw new IllegalArgumentException("Item " + item + " has incorrect dimension value.");
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
}
