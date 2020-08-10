package rtree.item.generic;

public interface IRType<T> {
	
	Object getData();
	void setData(String s);
	double distanceTo(T o);
	int compareTo(T o);
	
}
