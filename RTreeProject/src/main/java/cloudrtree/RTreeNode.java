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
public class RTreeNode {

	List<String> children;
	String parent;
//	List<LocationItem> locationItems;
	List<ILocationItem> locationItems;
//	Rectangle rectangle;
	IHyperRectangle rectangle;
	String nodeId;
	RTreeCache cache;
	private ILogger logger;

	public RTreeNode(String nodeId, String childrenStr, String parent, RTreeCache cache, ILogger logger) {
		this.cache = cache;
		this.logger = logger;
		this.nodeId = nodeId;
		
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
//		this.locationItems = new ArrayList<LocationItem>();
		this.locationItems = new ArrayList<ILocationItem>();
		
		
		this.rectangle = new Rectangle();
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

	@SuppressWarnings("unchecked")
	public JSONArray getChildrenJSON() {
		
		JSONArray arr = new JSONArray();
		if (children != null) {
			for (String s : children) {
				arr.add(s);
			}
		}
		return arr;
	}
	
	public JSONArray getItemsJSON() {
		
		return LocationItemBase.getItemsJSON(locationItems);
		
//		JSONArray arr = new JSONArray();
//		if (locationItems != null) {
//			for (LocationItem i : locationItems) {
//				
//				JSONObject obj = new JSONObject();
//				obj.put("x", i.getX());
//				obj.put("y", i.getY());
//				obj.put("type", i.getType());
//				arr.add(obj);
//			}
//		}
//		return arr;
	}

	public List<ILocationItem> items() {
		return locationItems;
	}

	@SuppressWarnings("unchecked")
	public void addItem(ILocationItem locationItem) throws IOException {
		
		logger.log("CloudRTreeNode.addItem");
		System.out.println("Rectangle: " + this.rectangle);
		locationItems.add(locationItem);
		updateRectangle();
		
		StringWriter out;
		
		JSONArray jsonArr = new JSONArray();
		for (ILocationItem item : this.items()) {
			jsonArr.add(item.getJson());
		}
		
		out = new StringWriter();
		jsonArr.writeJSONString(out);
	    String itemsStr = out.toString();
	    
	    
	    out = new StringWriter();
	    JSONObject jsonRect = this.getRectangle().getJson();
	    jsonRect.writeJSONString(out);
	    String rectStr = out.toString();
		
		cache.updateNode(this.nodeId, this.getChildrenJSON().toJSONString(), this.getParent(), itemsStr, rectStr);
		
		
	}
	public void updateRectangle() {
		updateRectangle(false);
	}
	public void updateRectangle(boolean goUp) {
		System.out.println("RTreeNode.UpdateRectangle rectangle: " + rectangle + ", rectangle.getDim1(0): " + rectangle.getDim1(0));
		int minX = rectangle.getDim1(0);
		int maxX = rectangle.getDim2(0);
		int minY = rectangle.getDim1(1);
		int maxY = rectangle.getDim2(1);

		if (locationItems.size() > 0) {
			minX = locationItems.get(0).getDim(0);
			maxX = minX;
			minY = locationItems.get(0).getDim(1);
			maxY = minY;
			for (int i = 0; i < locationItems.size(); i++) {
				if (locationItems.get(i).getDim(0) < minX) {
					minX = locationItems.get(i).getDim(0);
				}
				if (locationItems.get(i).getDim(0) > maxX) {
					maxX = locationItems.get(i).getDim(0);
				}
				if (locationItems.get(i).getDim(1) < minY) {
					minY = locationItems.get(i).getDim(1);
				}
				if (locationItems.get(i).getDim(1) > maxY) {
					maxY = locationItems.get(i).getDim(1);
				}
			}

		}

		rectangle.setDim1(0, minX);
		rectangle.setDim2(0, maxX);
		rectangle.setDim1(1, minY);
		rectangle.setDim2(1, maxY);
		
		cache.updateNode(nodeId, null, null, null, rectangle.getJson().toJSONString());
		logger.log("updated rectangle for " + nodeId + " new rectangle: " + rectangle);
		
		// logger.log("This node has a bounding box of " + rectangle.toString()
		// + " has parent? " + (parent != null) + "... child rect: " + childRectangle);

		if (parent != null && cache.getNode(parent) != null && goUp) {
			updateRectangle(cache.getNode(parent));
		}
	}
	
	public void updateRectangle(RTreeNode node) {
		
		logger.log("BRANCH UPDATE RECTANGLE:::: " + node.nodeId + " ... node.children: " + node.children + " node.parent: " + node.parent);
		
		IHyperRectangle childSum = node.rectangle;
		
		int minX = node.rectangle.getDim1(0);
		int maxX = node.rectangle.getDim2(0);
		int minY = node.rectangle.getDim1(1);
		int maxY = node.rectangle.getDim2(1);
		
		if (node.children != null) {
			RTreeNode firstChild = cache.getNode(node.getChildren().get(0));
			if (firstChild != null && firstChild.rectangle != null) {
				minX = firstChild.getRectangle().getDim1(0);
				minY = firstChild.getRectangle().getDim1(1);
				maxX = firstChild.getRectangle().getDim2(0);
				maxY = firstChild.getRectangle().getDim2(1);
			}
			
			
			if (firstChild != null) {
				childSum = firstChild.getRectangle();
			}
			
			for (String child : node.children) {
				
				RTreeNode childNode = cache.getNode(child);
				if (childNode != null && childNode.rectangle != null) {
					
					logger.log("childSum: " + childSum);
					IHyperRectangle childRectangle = cache.getNode(child).getRectangle();
					
					childSum = Rectangle.sumRectangles(childSum, childRectangle);
					
					if (minX > childRectangle.getDim1(0)) {
						minX = childRectangle.getDim1(0);
					}
					if (minY > childRectangle.getDim1(1)) {
						minY = childRectangle.getDim1(1);
					}
					if (maxX < childRectangle.getDim2(0)) {
						maxX = childRectangle.getDim2(0);
					}
					if (maxY < childRectangle.getDim2(1)) {
						maxY = childRectangle.getDim2(1);
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
		return children == null || children.size() == 0;
	}

	public IHyperRectangle getRectangle() {
		return rectangle;
	}

	public void setRectangle(IHyperRectangle rectangle) {
		this.rectangle = rectangle;
	}

	public List<String> getChildren() {
		if (children != null) {
			return children;
		} else {
			return new ArrayList<String>();
		}
	}

	public void setParent(String node) {
		this.parent = node;
	}

	public List<ILocationItem> getPoints() {
		return locationItems;
	}

	public String getParent() {
		return parent;
	}

	public void setChildren(List<String> newChildren) {
		children = newChildren;

	}
	
	@Override
	public String toString() {
		String str = "";
		str += "nodeId: " + nodeId + ", ";
		str += "children: " + this.getChildrenJSON().toJSONString() + ", ";
		str += "items: " + this.getItemsJSON().toJSONString() + ", ";
		str += "parent: " + this.parent + ", ";
		str += "rectangle: " + this.getRectangle().toString();
		return str;
	}

	public void setItemsJson(String items) {
		
		JSONParser parser = new JSONParser();
		Object obj;
		this.locationItems = new ArrayList<ILocationItem>();
		
		try {
			if (items != null && !items.equals("") && !items.equals("delete")) {
				
				obj = parser.parse(items);
				JSONArray arr = (JSONArray) obj;
				for (int i = 0; i < arr.size(); i++) {
					JSONObject row = (JSONObject) arr.get(i);
					ILocationItem item = new LocationItem(Integer.parseInt(row.get("x").toString()), Integer.parseInt(row.get("y").toString()), row.get("type").toString());
					this.locationItems.add(item);
				}
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			logger.log("items: |" + items + "|");
			e.printStackTrace();
		}
		
	}
	
	
}