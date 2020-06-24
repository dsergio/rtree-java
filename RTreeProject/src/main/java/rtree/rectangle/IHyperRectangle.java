package rtree.rectangle;

import java.util.List;

import org.json.simple.JSONObject;

import rtree.item.ILocationItem;

public interface IHyperRectangle {
	
	/**
	 * 
	 * @param dim Dimension. 0 for "x", 1 for "y", 2 for "z", etc.
	 * @param value
	 */
	public void setDim1(int dim, Integer value);
	
	/**
	 * 
	 * @param dim Dimension. 0 for "x", 1 for "y", 2 for "z", etc.
	 * @param value
	 */
	public void setDim2(int dim, Integer value);
	
	/**
	 * 
	 * @param dim Dimension. 0 for "x", 1 for "y", 2 for "z", etc.
	 * @return
	 */
	public Integer getDim1(int dim);
	
	/**
	 * 
	 * @param dim Dimension. 0 for "x", 1 for "y", 2 for "z", etc.
	 * @return
	 */
	public Integer getDim2(int dim);
	
	public List<Integer> getDimensionArray1();
	public List<Integer> getDimensionArray2();
	public int getNumberDimensions();
	public JSONObject getJson();
	public boolean containsPoint(ILocationItem item);
	public int getSpace();
	public int getLevel();
	public void setLevel(int level);
	
	
}
