package rtree.item.generic;

public class RInteger extends RType<Integer> implements IRType<RInteger> {

	public RInteger(Object data) {
		super((Integer)data);
	}
	
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


	
	

}
