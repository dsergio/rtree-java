package cloudrtree;

import org.json.simple.JSONObject;

public class HyperCuboid extends HyperRectangleBase {

	public HyperCuboid(int numberDimensions) {
		super(numberDimensions);
		if (numberDimensions < 4) {
			throw new IllegalArgumentException("HyperCuboid minimum dimension is 4.");
		} else {
			
		}
	}

	@Override
	public JSONObject getJson() {
		// TODO Auto-generated method stub
		return null;
	}

}
