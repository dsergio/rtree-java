package rtree.item.generic;

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
		// TODO Auto-generated method stub
		return 0;
	}

}
