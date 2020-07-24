package rtree.item;

public interface IRType<T> {
	
	double distanceTo(T o);
	int compareTo(T o);
	
}
