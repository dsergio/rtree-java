package rtree.tree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;

import rtree.item.ILocationItem;
import rtree.rectangle.IHyperRectangle;

/**
 * 
 * @author David Sergio
 *
 */
public interface IRTreeNode {

	public String getNodeId();
	public void setChildren(String childrenStr);
	public JSONArray getChildrenJSON();
	public JSONArray getItemsJSON();
	public List<ILocationItem> getLocationItems();
	public void addItem(ILocationItem locationItem) throws IOException;
	
	public void updateRectangle();
	public void updateRectangle(boolean goUp);
	public void updateRectangle(IRTreeNode node);
	
	public int getNumberOfItems();
	public boolean isLeafNode();
	public IHyperRectangle getRectangle();
	public void setRectangle(IHyperRectangle rectangle);
	public List<String> getChildren();
	public void setParent(String node);
	public List<ILocationItem> getPoints();
	public String getParent();
	public void setChildren(List<String> newChildren);
	public String toString();
	
	
	public void setItemsJson(String items);
	public void setLocationItems(ArrayList<ILocationItem> arrayList);

}