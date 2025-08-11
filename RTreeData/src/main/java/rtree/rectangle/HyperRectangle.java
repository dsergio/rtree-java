package rtree.rectangle;

import rtree.item.IRType;

/**
 * HyperRectangle class represents a hyper-rectangle in an R-tree structure.
 * @param <T> Type of the items stored in the hyper-rectangle, extending IRType.
 */
public class HyperRectangle<T extends IRType<T>> extends HyperRectangleBase<T> implements IHyperRectangle<T> {
	
	/**
	 * Constructor for HyperRectangle.
	 * @param numberDimensions the number of dimensions for the hyper-rectangle
	 * @throws IllegalArgumentException if the number of dimensions is less than 1
	 */
	public HyperRectangle(int numberDimensions) throws IllegalArgumentException {
		super(numberDimensions);
		if (numberDimensions < 1) {
			throw new IllegalArgumentException("HyperCuboid minimum dimension is 1.");
		} else {
			
		}
	}

}
