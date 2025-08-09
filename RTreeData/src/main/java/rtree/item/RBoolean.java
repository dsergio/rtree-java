package rtree.item;

/**
 * RBoolean class representing a boolean type in the R-Tree item structure.
 * It extends the RType class and implements the IRType interface.
 * 
 */
public class RBoolean extends RType<Boolean> implements IRType<RBoolean>  {
	
	/**
	 * Constructor that initializes the RBoolean with a boolean value.
	 * @param data the boolean value to set
	 */
	public RBoolean(Object data) {
		super((Boolean)data);
	}
	
	/**
	 * Default constructor that initializes the RBoolean with null.
	 */
	public RBoolean() {
		super(null);
	}
	
	/**
	 * Sets the data for this RBoolean instance.
	 * @param s the string representation of the boolean value to set
	 */
	@Override
	public void setData(String s) {
		data = Boolean.parseBoolean(s);
	}
	
	/**
	 * Get the distance to another RBoolean instance.
	 * @param o the other RBoolean instance to compare with
	 * @return the distance, which is 1 if both are true or both are false, and 0 otherwise
	 */
	@Override
	public double distanceTo(RBoolean o) {
		if (data && o.getData()) {
			return 1;
		}
		if (!data && !o.getData()) {
			return 1;
		}
		return 0;
	}
	
	/**
	 * Compare this RBoolean instance with another RBoolean instance.
	 * @param o the other RBoolean instance to compare with
	 * @return the comparison result, which is based on the boolean value
	 */
	@Override
	public int compareTo(RBoolean o) {
		return data.compareTo(o.data);
	}
	
	/**
	 * Get whether this RBoolean has a value range.
	 * @return false, as boolean values do not have a range
	 */
	@Override
	public boolean hasValueRange() {
		return false;
	}

	/**
	 * Get the minimum value for this RBoolean type.
	 * @return null, as boolean does not have a minimum value
	 */
	@Override
	public RBoolean getValueMin() {
		return null;
	}
	
	/**
	 * Get the maximum value for this RBoolean type.
	 * @return null, as boolean does not have a maximum value
	 */
	@Override
	public RBoolean getValueMax() {
		return null;
	}

	/**
	 * Check if this RBoolean has discrete values.
	 * @return true, as boolean values are discrete (true or false)
	 */
	@Override
	public boolean hasDiscreteValues() {
		return true;
	}
	
	

}
