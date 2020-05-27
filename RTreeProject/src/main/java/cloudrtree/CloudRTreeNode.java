package cloudrtree;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


/**
 * 
 * Description TBD
 * 
 * @author David Sergio
 *
 */
public class CloudRTreeNode {

	List<String> children;
	String parent;
	List<LocationItem> locationItems;
	Rectangle rectangle;
	String nodeId;
	CloudRTreeCache cache;

	public CloudRTreeNode(String nodeId, String childrenStr, String parent, CloudRTreeCache cache) {
		this.cache = cache;
		
		this.nodeId = nodeId;
		
		if (cache == null) {
			System.out.println("Cache is null...");
		}
		
//		System.out.println("==|children: " + children + " ... " + childrenStr);
		JSONParser parser = new JSONParser();
		Object obj;
		try {
			if (childrenStr != null && !childrenStr.equals("{}") && !childrenStr.equals("")) {
				if (children == null) {
					children = new ArrayList<String>();
				}
				obj = parser.parse(childrenStr);
				
				JSONArray jsonArr = (JSONArray) obj;
				if (jsonArr.size() > 0) {
					
					for (int i = 0; i < jsonArr.size(); i++) {
						children.add(jsonArr.get(i).toString());
					}
				}
			}
			
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
		
		
		this.parent = parent;
		this.locationItems = new ArrayList<LocationItem>();
		
		
		
		
		setRectangle(new Rectangle());
	}
	
	public void setChildren(String childrenStr) {
		JSONParser parser = new JSONParser();
		Object obj;
		try {
			if (childrenStr != null && !childrenStr.equals("{}")) {
				children = new ArrayList<String>();
				
				obj = parser.parse(childrenStr);
				
				JSONArray jsonArr = (JSONArray) obj;
				if (jsonArr.size() > 0) {
					
					for (int i = 0; i < jsonArr.size(); i++) {
						children.add(jsonArr.get(i).toString());
					}
				}
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public JSONArray getChildrenJSON() {
		
		JSONArray arr = new JSONArray();
		for (String s : children) {
			arr.add(s);
		}
		return arr;
	}
	
	public JSONArray getItemsJSON() {
		
		JSONArray arr = new JSONArray();
		for (LocationItem i : locationItems) {
			
			JSONObject obj = new JSONObject();
			obj.put("x", i.getX());
			obj.put("y", i.getY());
			obj.put("type", i.getType());
			arr.add(obj);
		}
		return arr;
	}

	public List<LocationItem> items() {
		return locationItems;
	}

	public void addItem(LocationItem locationItem, CloudRTreeNode node) throws IOException {
		
		System.out.println("CloudRTreeNode - addItem");
		locationItems.add(locationItem);
		updateRectangle();
		
		StringWriter out;
		
		JSONArray jsonArr = new JSONArray();
		for (LocationItem item : node.items()) {
			jsonArr.add(item.getJson());
		}
		out = new StringWriter();
		jsonArr.writeJSONString(out);
	    String itemsStr = out.toString();
	    
	    
	    out = new StringWriter();
	    JSONObject jsonRect = node.getRectangle().getJson();
	    jsonRect.writeJSONString(out);
	    String rectStr = out.toString();
		
		cache.updateNode(node.nodeId, null, null, itemsStr, rectStr);
		
		
	}
	public void updateRectangle() {
		updateRectangle(false);
	}
	public void updateRectangle(boolean goUp) {
		int minX = rectangle.getX1();
		int maxX = rectangle.getX2();
		int minY = rectangle.getY1();
		int maxY = rectangle.getY2();

		if (locationItems.size() > 0) {
			minX = locationItems.get(0).getX();
			maxX = minX;
			minY = locationItems.get(0).getY();
			maxY = minY;
			for (int i = 0; i < locationItems.size(); i++) {
				if (locationItems.get(i).getX() < minX) {
					minX = locationItems.get(i).getX();
				}
				if (locationItems.get(i).getX() > maxX) {
					maxX = locationItems.get(i).getX();
				}
				if (locationItems.get(i).getY() < minY) {
					minY = locationItems.get(i).getY();
				}
				if (locationItems.get(i).getY() > maxY) {
					maxY = locationItems.get(i).getY();
				}
			}

		}

		rectangle.setX1(minX);
		rectangle.setX2(maxX);
		rectangle.setY1(minY);
		rectangle.setY2(maxY);

		// System.out.println("This node has a bounding box of " + rectangle.toString()
		// + " has parent? " + (parent != null) + "... child rect: " + childRectangle);

		if (parent != null && cache.getNode(parent) != null && goUp) {
			updateRectangle(cache.getNode(parent));
		}
	}
	
	public void updateRectangle(CloudRTreeNode node) {
		
		System.out.println("BRANCH UPDATE RECTANGLE:::: " + node.nodeId + " ... node.children: " + node.children + " node.parent: " + node.parent);
		
		Rectangle childSum = node.rectangle;
		
		int minX = node.rectangle.getX1();
		int maxX = node.rectangle.getX2();
		int minY = node.rectangle.getY1();
		int maxY = node.rectangle.getY2();
		
		if (node.children != null) {
			CloudRTreeNode firstChild = cache.getNode(node.getChildren().get(0));
			if (firstChild != null && firstChild.rectangle != null) {
				minX = firstChild.getRectangle().getX1();
				minY = firstChild.getRectangle().getY1();
				maxX = firstChild.getRectangle().getX2();
				maxY = firstChild.getRectangle().getY2();
			}
			
			
			if (firstChild != null) {
				childSum = firstChild.getRectangle();
			}
			
			for (String child : node.children) {
				
				CloudRTreeNode childNode = cache.getNode(child);
				if (childNode != null && childNode.rectangle != null) {
					
					System.out.println("childSum: " + childSum);
					Rectangle childRectangle = cache.getNode(child).getRectangle();
					
					childSum = Rectangle.sumRectangles(childSum, childRectangle);
					
					if (minX > childRectangle.getX1()) {
						minX = childRectangle.getX1();
					}
					if (minY > childRectangle.getY1()) {
						minY = childRectangle.getY1();
					}
					if (maxX < childRectangle.getX2()) {
						maxX = childRectangle.getX2();
					}
					if (maxY < childRectangle.getY2()) {
						maxY = childRectangle.getY2();
					}
					
				}
				
				
			}
			
			node.rectangle = childSum;
			cache.updateNode(node.nodeId, null, null, null, node.rectangle.getJson().toJSONString());
		}
		
		

//		node.rectangle.setX1(minX);
//		node.rectangle.setX2(maxX);
//		node.rectangle.setY1(minY);
//		node.rectangle.setY2(maxY);
		
		if (node.parent != null && cache.getNode(node.parent) != null) {
			updateRectangle(cache.getNode(node.parent));
		}
		
	}
	

	public int getNumberOfItems() {
		return locationItems.size();
	}

	public boolean isLeafNode() {
		return children == null;
	}

	public Rectangle getRectangle() {
		return rectangle;
	}

	public void setRectangle(Rectangle rectangle) {
		this.rectangle = rectangle;
	}

	public List<String> getChildren() {
		return children;
	}

	public void setParent(String node) {
		this.parent = node;
	}

	public List<LocationItem> getPoints() {
		return locationItems;
	}

	public String getParent() {
		return parent;
	}

	public void setChildren(List<String> newChildren) {
		children = newChildren;

	}
	
}