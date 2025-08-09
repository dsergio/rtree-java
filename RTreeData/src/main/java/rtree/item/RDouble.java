package rtree.item;

//import java.math.BigDecimal;
//import java.math.RoundingMode;

/**
 * Represents a double-precision floating-point number in the RTree.
 * This class extends RType and implements IRType for handling double values.
 * It provides methods for setting data, calculating distance, and comparing values.
 */
public class RDouble extends RType<Double> implements IRType<RDouble> {
	
	/**
	 * Constructs an RDouble with the specified data.
	 * 
	 * @param data the double value to set
	 */
	public RDouble(Object data) {
		super((Double)data);
	}
	
	/**
	 * Default constructor for RDouble, initializes with null data.
	 */
	public RDouble() {
		super(null);
	}
	
	/**
	 * Sets the data for this RDouble instance from a string.
	 * @param s the string representation of the double value
	 */
	@Override
	public void setData(String s) {
		data = Double.parseDouble(s);
	}

	@Override
	public double distanceTo(RDouble o) {
		return Math.abs(data - o.data);
	}

	@Override
	public int compareTo(RDouble o) {
		return data.compareTo(o.data);
	}

	@Override
	public boolean hasValueRange() {
		return true;
	}

	@Override
	public RDouble getValueMin() {
		return new RDouble(Double.MIN_VALUE);
	}

	@Override
	public RDouble getValueMax() {
		return new RDouble(Double.MAX_VALUE);
	}

	@Override
	public boolean hasDiscreteValues() {
		return false;
	}
	
	@Override
	public Double getData() {
		
		if (data == null) {
			return null;
		} else {
			return data.doubleValue();
		}
		
//		BigDecimal bd = new BigDecimal(data);
//        bd = bd.setScale(3, RoundingMode.HALF_UP);
//        return bd.doubleValue();
        
	}

}
