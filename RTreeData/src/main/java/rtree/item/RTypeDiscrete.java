package rtree.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * RTypeDiscrete abstract class represents a discrete type of data in an R-tree
 * structure. It extends the RType class and provides methods to manage discrete
 * values and their distances.
 * 
 * @param <T> the type of data represented by this RTypeDiscrete
 */
public abstract class RTypeDiscrete<T> extends RType<T> {
	
	/**
	 * The data item of this RTypeDiscrete.
	 */
	protected T data;
	
	/**
	 * List of discrete values associated with this RTypeDiscrete.
	 */
	protected List<T> values;
	
	/**
	 * Map to store the rank of each discrete value. The key is the discrete value,
	 * and the value is its rank.
	 */
	protected Map<T, Integer> rank;
	
	/**
	 * Distance matrix to store distances between discrete values. The matrix is
	 * symmetric, where distanceMatrix[i][j] is the distance between values[i] and
	 * values[j].
	 */
	protected double[][] distanceMatrix;

	/**
	 * Constructor for RTypeDiscrete.
	 * 
	 * @param data the data item of this RTypeDiscrete
	 */
	public RTypeDiscrete(T data) {
		super(data);
		values = new ArrayList<T>();
		rank = new HashMap<T, Integer>();
		distanceMatrix = new double[0][0];
	}
	
	@Override
	public T getData() {
		return data;
	}
	
	public abstract void setData(String s);
	
	/**
	 * Adds a discrete value to the list of values if it is not already present.
	 * @param value the discrete value to add
	 */
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
	
	/**
	 * Adds a distance between two discrete values to the distance matrix.
	 */
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
	
	/**
	 * Sets the rank of a discrete value in the rank map.
	 * @param item
	 * @param rankValue
	 */
	protected void setRank(T item, Integer rankValue) {
		if (rank.containsKey(item)) {
			rank.put(item, rankValue);
		}
	}
	
	/**
	 * Get a list of discrete values.
	 * @return a list of discrete values, each of type T
	 */
	protected List<T> getDiscreteValues() {
		return values;
	}

}
