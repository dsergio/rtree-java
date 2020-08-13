package rtree.item.generic;

public abstract class RType<T> {

	protected T data;

	public RType(T data) {
		this.data = data;
	}

	public T getData() {
		return data;
	}
	
	public abstract void setData(String s);
	

}
