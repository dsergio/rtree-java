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

	 String getNodeId();
	 void setChildren(String childrenStr);
	 JSONArray getChildrenJSON();
	 JSONArray getItemsJSON();
	 List<ILocationItem> getLocationItems();
	 void addItem(ILocationItem locationItem) throws IOException;
	
	 void updateRectangle();
	 void updateRectangle(boolean goUp);
	 void updateRectangle(IRTreeNode node);
	
	 int getNumberOfItems();
	 boolean isLeafNode();
	 IHyperRectangle getRectangle();
	 void setRectangle(IHyperRectangle rectangle);
	 List<String> getChildren();
	 void setParent(String node);
	 List<ILocationItem> getPoints();
	 String getParent();
	 void setChildren(List<String> newChildren);
	 String toString();
	
	
	 void setItemsJson(String items);
	 void setLocationItems(ArrayList<ILocationItem> arrayList);

}