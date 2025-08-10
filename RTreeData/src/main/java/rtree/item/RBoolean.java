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
	
	@Override
	public void setData(String s) {
		data = Boolean.parseBoolean(s);
	}
	
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
	
	@Override
	public int compareTo(RBoolean o) {
		return data.compareTo(o.data);
	}
	
	@Override
	public boolean hasValueRange() {
		return false;
	}
	
	@Override
	public RBoolean getValueMin() {
		return null;
	}
	
	@Override
	public RBoolean getValueMax() {
		return null;
	}
	
	@Override
	public boolean hasDiscreteValues() {
		return true;
	}
	
	

}
