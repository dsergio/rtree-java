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
 * RTreeNode interface represents a node in an R-tree structure.
 * @param <T> {@link rtree.item.IRType}
 * @author David Sergio
 */
public interface IRTreeNode<T extends IRType<T>> {
	
	/**
	 * Get the ID of the node.
	 * @return the ID of the node
	 */
	String getNodeId();
	
	/**
	 * Get a short version of the node ID.
	 * @return a short version of the node ID
	 */
	String getNodeIdShort();
	
	/**
	 * Get the string representation of the node.
	 * @return a string representation of the node
	 */
	String toString();
	
	/**
	 * Get the number of items in the node.
	 * @return the number of items in the node
	 */
	int getNumberOfItems();
	
	/**
	 * Get whether the node is a leaf node.
	 * @return true if the node is a leaf node, false otherwise
	 */
	boolean isLeafNode();
	
	/**
	 * Get a list of children node IDs.
	 * @return a list of children node IDs
	 */
	List<String> getChildren();
	
	/**
	 * Get a short list of children node IDs.
	 * @return a short list of children node IDs
	 */
	List<String> getChildrenShort();
	
	/**
	 * set the children of this node to the given list of children IDs.
	 * @param children the list of children IDs to set
	 */
	void setChildren(List<String> children);
	
	/**
	 * Get the children of this node as a JSON array.
	 * @return a JSON array of children node IDs
	 */
	JSONArray getChildrenJSON();
	
	/**
	 * Set children of this node from a JSON string.
	 * @param childrenJSONString the JSON string representing the children node IDs
	 */
	void setChildrenJSON(String childrenJSONString);
	
	/**
	 * Update the rectangle based on minimum bounding rectangle of the items in the node.
	 */
	void updateRectangle();
	
	/**
	 * Update the rectangle based on the minimum bounding rectangle of the items in the node,
	 * optionally going up in the tree.
	 * @param goUp true to go up in the tree, false otherwise
	 */
	void updateRectangle(boolean goUp);
	
	/**
	 * Update the rectangle of the node
	 * @param node the IRTreeNode to update the rectangle for
	 */
	void updateRectangle(IRTreeNode<T> node);
	
	/**
	 * Get the rectangle of the node.
	 * @return the rectangle of the node
	 */
	IHyperRectangle<T> getRectangle();
	
	/**
	 * Set the rectangle of the node.
	 * @param rectangle the rectangle to set for the node
	 */
	void setRectangle(IHyperRectangle<T> rectangle);
	
	/**
	 * Get the parent node ID.
	 * @return the parent node ID
	 */
	String getParent();
	
	/**
	 * Set the parent node ID.
	 * @param nodeId the parent node ID to set
	 */
	void setParent(String nodeId);
	
	/**
	 * Get the items in the node as a JSON array.
	 * @return a JSON array of items in the node
	 */
	JSONArray getLocationItemsJSON();
	
	/**
	 * Set the items in the node from a JSON string.
	 * @param itemsJSONString the JSON string representing the items in the node
	 */
	void setLocationItemsJson(String itemsJSONString);
	
	/**
	 * Get the items in the node.
	 * @return a list of items in the node
	 */
	List<ILocationItem<T>> getLocationItems();
	
	/**
	 * Set the items in the node.
	 * @param arrayList the list of items to set in the node
	 */
	void setLocationItems(ArrayList<ILocationItem<T>> arrayList);
	
	/**
	 * Add a location item to the node.
	 * @param locationItem the location item to add
	 * @throws IOException if an error occurs while adding the item
	 */
	void addLocationItem(ILocationItem<T> locationItem) throws IOException;
	
}