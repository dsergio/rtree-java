package rtree.item;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class RDouble extends RType<Double> implements IRType<RDouble> {
	
	public RDouble(Object data) {
		super((Double)data);
	}
	
	public RDouble() {
		super(null);
	}
	
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
		
		BigDecimal bd = new BigDecimal(data);
        bd = bd.setScale(3, RoundingMode.HALF_UP);
        return bd.doubleValue();
        
	}

}
