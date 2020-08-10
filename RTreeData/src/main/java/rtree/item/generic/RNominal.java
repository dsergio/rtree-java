package rtree.item.generic;

import java.util.ArrayList;
import java.util.List;

public class RNominal extends RType<List<String>> implements IRType<RNominal> {

	public RNominal(Object data) {
		super((List<String>)data);
	}
	
	public RNominal() {
		super(null);
	}
	
	@Override
	public void setData(String s) {
		data = new ArrayList<String>();
	}

	@Override
	public double distanceTo(RNominal o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int compareTo(RNominal o) {
		// TODO Auto-generated method stub
		return 0;
	}


	
	

}
