package rtree.item;

public interface IRType<T> {
	
	Object getData();
	void setData(String s);
	double distanceTo(T o);
	int compareTo(T o);
	
	boolean hasValueRange();
	T getValueMin();
	T getValueMax();
	
	boolean hasDiscreteValues();
	
	
}
