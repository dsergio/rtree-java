package rtree.item;

/**
 * Abstract class representing a nominal type in R-tree.
 */
public abstract class RNominal extends RTypeDiscrete<String> {
	
	/**
	 * Constructor for RNominal.
	 * @param data the nominal data
	 */
	public RNominal(Object data) {
		super((String)data);
	}
	
	/**
	 * Default constructor for RNominal. Initializes with null data.
	 */
	public RNominal() {
		super(null);
	}
	
	@Override
	public void setData(String s) {
		System.out.println("in setData... s: " + s + " values.contains(s): " + values.contains(s) + " values: " + values);
		if (values.contains(s)) {
			data = s;
		}
	}

}
