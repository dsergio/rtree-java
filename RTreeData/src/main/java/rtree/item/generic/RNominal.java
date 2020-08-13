package rtree.item.generic;

public abstract class RNominal extends RTypeDiscrete<String> {

	public RNominal(Object data) {
		super((String)data);
	}
	
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
