package rtree.item;

/**
 * RType abstract class represents a generic type for R-tree items.
 * @param <T> the type of data contained in the RType
 */
public abstract class RType<T> {
	
	/**
	 * The data contained in this RType.
	 */
	protected T data;
	
	/**
	 * Constructor for RType.
	 * 
	 * @param data the data to be contained in this RType
	 */
	public RType(T data) {
		this.data = data;
	}
	
	/**
	 * Get the data contained in this RType.
	 * 
	 * @return the data of type T
	 */
	public T getData() {
		return data;
	}
	
	/**
	 * Set the data contained in this RType.
	 * 
	 * @param s the data to be set
	 */
	public abstract void setData(String s);
	
}
