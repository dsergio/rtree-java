package rtree.item;

public class RBoolean extends RType<Boolean> implements IRType<RBoolean>  {

	public RBoolean(Object data) {
		super((Boolean)data);
	}
	
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
