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
	 String getNodeIdShort();
	 String toString();
	 int getNumberOfItems();
	 boolean isLeafNode();
	 
	 List<String> getChildren();
	 List<String> getChildrenShort();
	 void setChildren(List<String> children);
	 JSONArray getChildrenJSON();
	 void setChildrenJSON(String childrenJSONString);
	
	 void updateRectangle();
	 void updateRectangle(boolean goUp);
	 void updateRectangle(IRTreeNode<T> node);
	 
	 IHyperRectangle<T> getRectangle();
	 void setRectangle(IHyperRectangle<T> rectangle);
	 
	 String getParent();
	 void setParent(String nodeId);
	 
	 JSONArray getLocationItemsJSON();
	 void setLocationItemsJson(String itemsJSONString);
	 List<ILocationItem<T>> getLocationItems();
	 void setLocationItems(ArrayList<ILocationItem<T>> arrayList);
	 
	 void addLocationItem(ILocationItem<T> locationItem) throws IOException;	 
	 

}