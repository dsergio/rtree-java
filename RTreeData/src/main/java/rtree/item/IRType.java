package rtree.item;

public interface IRType<T> {
	
	public double distanceTo(T o);
	public int compareTo(T o);
	
}
