package rtree.item;

public class RInteger extends RType<Integer> implements IRType<RInteger> {

	public RInteger(Integer data) {
		super(data);
	}

	@Override
	public double distanceTo(RInteger o) {
		return Math.abs(o.getData() - data);
	}

	@Override
	public int compareTo(RInteger o) {
		// TODO Auto-generated method stub
		return 0;
	}


	
	

}
