package rtree.item;

/**
 * RInteger class represents an integer type for R-tree items.
 * It extends the RType class and implements the IRType interface.
 */
public class RInteger extends RType<Integer> implements IRType<RInteger> {
	
	/**
	 * Constructor for RInteger that accepts an Integer value.
	 * @param data the Integer value to set
	 */
	public RInteger(Object data) {
		super((Integer)data);
	}
	
	/**
	 * Default constructor for RInteger that initializes the data to null.
	 */
	public RInteger() {
		super(null);
	}
	
	@Override
	public void setData(String s) {
		data = Integer.parseInt(s);
	}

	@Override
	public double distanceTo(RInteger o) {
		return Math.abs(o.getData() - data);
	}

	@Override
	public int compareTo(RInteger o) {
		return data.compareTo(o.data);
	}

	@Override
	public boolean hasValueRange() {
		return true;
	}

	@Override
	public RInteger getValueMin() {
		return new RInteger(Integer.MIN_VALUE);
	}

	@Override
	public RInteger getValueMax() {
		return new RInteger(Integer.MAX_VALUE);
	}

	@Override
	public boolean hasDiscreteValues() {
		return false;
	}

}
