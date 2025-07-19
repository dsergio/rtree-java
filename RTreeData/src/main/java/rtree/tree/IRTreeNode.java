package rtree.tree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;

import rtree.item.ILocationItem;
import rtree.item.IRType;
import rtree.rectangle.IHyperRectangle;

/**
 * 
 * @author David Sergio
 *
 */
public interface IRTreeNode<T extends IRType<T>> {

	 String getNodeId();
	 void setChildren(String childrenStr);
	 JSONArray getChildrenJSON();
	 JSONArray getItemsJSON();
	 List<ILocationItem<T>> getLocationItems();
	 void addItem(ILocationItem<T> locationItem) throws IOException;
	
	 void updateRectangle();
	 void updateRectangle(boolean goUp);
	 void updateRectangle(IRTreeNode<T> node);
	
	 int getNumberOfItems();
	 boolean isLeafNode();
	 IHyperRectangle<T> getRectangle();
	 void setRectangle(IHyperRectangle<T> rectangle);
	 List<String> getChildren();
	 void setParent(String node);
	 List<ILocationItem<T>> getPoints();
	 String getParent();
	 void setChildren(List<String> newChildren);
	 String toString();
	
	
	 void setItemsJson(String items);
	 void setLocationItems(ArrayList<ILocationItem<T>> arrayList);

}