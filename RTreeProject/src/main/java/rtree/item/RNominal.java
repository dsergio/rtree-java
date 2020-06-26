package rtree.item;

import java.util.List;

public class RNominal extends RType<List<String>> implements IRType<RNominal> {

	public RNominal(List<String> data) {
		super(data);
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
