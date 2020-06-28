package rtree.item;

public class RBoolean extends RType<Boolean> implements IRType<RBoolean>  {

	public RBoolean(Boolean data) {
		super(data);
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
