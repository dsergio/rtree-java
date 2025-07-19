package rtree.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class RTypeDiscrete<T> extends RType<T> {

	protected T data;
	protected List<T> values;
	protected Map<T, Integer> rank;
	protected double[][] distanceMatrix;

	public RTypeDiscrete(T data) {
		super(data);
		values = new ArrayList<T>();
		rank = new HashMap<T, Integer>();
		distanceMatrix = new double[0][0];
	}

	public T getData() {
		return data;
	}
	
	public abstract void setData(String s);
	
	protected void addValue(T value) {
		if (!values.contains(value)) {
			values.add(value);
			rank.put(value, 0);
			
//			System.out.print("distanceMatrix == null ");
//			System.out.println(distanceMatrix == null);
//			System.out.println("distanceMatrix.length: " + distanceMatrix.length);
			
			double temp[][] = new double[values.size()][values.size()];
			for (int i = 0; i < distanceMatrix.length; i++) {
//				System.out.println("distanceMatrix[" + i + "]: " + distanceMatrix[i]);
				temp[i] = Arrays.copyOf(distanceMatrix[i], distanceMatrix[i].length + 1);
			}
			distanceMatrix = temp;
			
			
//			distanceMatrix = Arrays.copyOf(distanceMatrix, distanceMatrix.length + 1);
//			System.out.print("after distanceMatrix: ");
//			System.out.println(Arrays.deepToString(distanceMatrix));
//			System.out.println();
			
		}
	}
	protected void addValueDistance(T from, T to, double distance) {
		addValueDistance(values.indexOf(from), values.indexOf(to), distance);
	}
	private void addValueDistance(int indexFrom, int indexTo, double distance) {
		if (indexFrom < values.size() && indexFrom >= 0 && indexTo < values.size() && indexTo >= 0) {
			distanceMatrix[indexFrom][indexTo] = distance;
			distanceMatrix[indexTo][indexFrom] = distance;
		} else {
			throw new IllegalArgumentException("index values are invalid");
		}
	}
	
	protected void setRank(T item, Integer rankValue) {
		if (rank.containsKey(item)) {
			rank.put(item, rankValue);
		}
	}
	
	List<T> getDiscreteValues() {
		return values;
	}

}
