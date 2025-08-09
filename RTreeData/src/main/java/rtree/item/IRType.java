package rtree.item;

/**
 * Interface for RTree item types.
 * @param <T> Type of the data enclosed in the type.
 */
public interface IRType<T> {
	
	/**
	 * Get the enclosed data of this type.
	 * @return the data object
	 */
	Object getData();
	
	/**
	 * Set the enclosed data of this type.
	 * @param s the data to set
	 */
	void setData(String s);
	
	/**
	 * Calculate the distance to another item of the same type.
	 * @param o the other item to compare with
	 * @return the distance between this item and the other item
	 */
	double distanceTo(T o);
	
	/**
	 * Compare this item with another item of the same type.
	 * @param o the other item to compare with
	 * @return a negative integer, zero, or a positive integer as this item is less than, equal to, or greater than the specified object
	 */
	int compareTo(T o);
	
	/**
	 * Check if this type has a value range.
	 * @return true if the type has a value range, false otherwise
	 */
	boolean hasValueRange();
	
	/**
	 * Get the minimum value of this type.
	 * @return the minimum value
	 */
	T getValueMin();
	
	/**
	 * Get the maximum value of this type.
	 * @return the maximum value
	 */
	T getValueMax();
	
	/**
	 * Check if this type has discrete values.
	 * @return true if the type has discrete values, false otherwise
	 */
	boolean hasDiscreteValues();
	
}
