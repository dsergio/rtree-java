package rtree.tree.generic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;

import rtree.item.ILocationItem;
import rtree.item.generic.ILocationItemGeneric;
import rtree.item.generic.IRType;
import rtree.rectangle.IHyperRectangle;
import rtree.rectangle.generic.IHyperRectangleGeneric;

/**
 * 
 * @author David Sergio
 *
 */
public interface IRTreeNodeGeneric<T extends IRType<T>> {

	 String getNodeId();
	 void setChildren(String childrenStr);
	 JSONArray getChildrenJSON();
	 JSONArray getItemsJSON();
	 List<ILocationItemGeneric<T>> getLocationItems();
	 void addItem(ILocationItemGeneric<T> locationItem) throws IOException;
	
	 void updateRectangle();
	 void updateRectangle(boolean goUp);
	 void updateRectangle(IRTreeNodeGeneric<T> node);
	
	 int getNumberOfItems();
	 boolean isLeafNode();
	 IHyperRectangleGeneric<T> getRectangle();
	 void setRectangle(IHyperRectangleGeneric<T> rectangle);
	 List<String> getChildren();
	 void setParent(String node);
	 List<ILocationItemGeneric<T>> getPoints();
	 String getParent();
	 void setChildren(List<String> newChildren);
	 String toString();
	
	
	 void setItemsJson(String items);
	 void setLocationItems(ArrayList<ILocationItemGeneric<T>> arrayList);

}