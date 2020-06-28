package rtree.item;

public class RType<T> {
	
	protected final T data;
	
	public RType(T data) {
		this.data = data;
	}

	public T getData() {
		return data;
	}
	
}
