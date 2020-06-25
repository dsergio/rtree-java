package rtree.tree;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import rtree.item.ILocationItem;
import rtree.item.LocationItem2D;
import rtree.item.LocationItemBase;
import rtree.item.LocationItemND;
import rtree.log.ILogger;
import rtree.rectangle.RectangleND;
import rtree.rectangle.HyperRectangleBase;
import rtree.rectangle.IHyperRectangle;
import rtree.rectangle.Rectangle2D;


/**
 * 
 * TODO deprecate
 * 
 * @author David Sergio
 *
 */
public class DepRTreeNode {

	List<String> children;
	String parent;
	List<ILocationItem> locationItems;
	IHyperRectangle rectangle;
	String nodeId;
	DepIRTreeCache cache;
	private ILogger logger;

	public DepRTreeNode(String nodeId, String childrenStr, String parent, DepIRTreeCache cache, ILogger logger) {
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
		
		if (cache != null) {
			this.rectangle = new RectangleND(cache.getNumDimensions());
		}
//		this.rectangle = new Rectangle();
	}
	
	public String getNodeId() {
		return nodeId;
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
	}

	public List<ILocationItem> getLocationItems() {
		return locationItems;
	}

	@SuppressWarnings("unchecked")
	public void addItem(ILocationItem locationItem) throws IOException {
		
		logger.log("CloudRTreeNode.addItem");
		System.out.println("Rectangle: " + this.rectangle);
		locationItems.add(locationItem);
		updateRectangleNDimensional();
		
		StringWriter out;
		
		JSONArray jsonArr = new JSONArray();
		for (ILocationItem item : locationItems) {
			jsonArr.add(item.getJson());
		}
		
		out = new StringWriter();
		jsonArr.writeJSONString(out);
	    String itemsStr = out.toString();
	    
	    
	    out = new StringWriter();
	    JSONObject jsonRect = this.getRectangle().getJson();
	    jsonRect.writeJSONString(out);
	    String rectStr = out.toString();
		
		cache.updateNodeNDimensional(this.nodeId, this.getChildrenJSON().toJSONString(), this.getParent(), itemsStr, rectStr);
		
		
	}
	public void updateRectangle2D() {
		updateRectangle2D(false);
	}
	public void updateRectangleNDimensional() {
		updateRectangleNDimensional(false);
	}
	public void updateRectangle2D(boolean goUp) {
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
		
		cache.updateNode2D(nodeId, null, null, null, rectangle.getJson().toJSONString());
		logger.log("updated rectangle for " + nodeId + " new rectangle: " + rectangle);
		
		// logger.log("This node has a bounding box of " + rectangle.toString()
		// + " has parent? " + (parent != null) + "... child rect: " + childRectangle);

		if (parent != null && cache.getNode(parent) != null && goUp) {
			updateRectangle2D(cache.getNode(parent));
		}
	}
	
	public void updateRectangleNDimensional(boolean goUp) {
		System.out.println(rectangle.getNumberDimensions() + "-Dimensional RTreeNode.UpdateRectangle rectangle: " + rectangle + ", rectangle.getDim1(0): " + rectangle.getDim1(0));
		
		List<Integer> minimums = new ArrayList<Integer>();
		List<Integer> maximums = new ArrayList<Integer>();
		for (int i = 0; i < rectangle.getNumberDimensions(); i++) {
			minimums.add(null);
			maximums.add(null);
		}
		for (int i = 0; i < rectangle.getNumberDimensions(); i++) {
			minimums.set(i, rectangle.getDim1(i));
			maximums.set(i, rectangle.getDim2(i));
		}
		
		if (locationItems.size() > 0) {
			for (int i = 0; i < rectangle.getNumberDimensions(); i++) {
				minimums.set(i, locationItems.get(0).getDim(i));
				maximums.set(i, minimums.get(i));
			}
			
			for (int i = 0; i < locationItems.size(); i++) {
				for (int i2 = 0; i2 < rectangle.getNumberDimensions(); i2++) {
					if (locationItems.get(i).getDim(i2) < minimums.get(i2)) {
						minimums.set(i2, locationItems.get(i).getDim(i2));
					}
					if (locationItems.get(i).getDim(i2) > maximums.get(i2)) {
						maximums.set(i2, locationItems.get(i).getDim(i2));
					}
				}
			}
		}
		
		for (int i = 0; i < rectangle.getNumberDimensions(); i++) {
			rectangle.setDim1(i, minimums.get(i));
			rectangle.setDim2(i, maximums.get(i));
		}
		
		cache.updateNodeNDimensional(nodeId, null, null, null, rectangle.getJson().toJSONString());
		logger.log("updated rectangle for " + nodeId + " new rectangle: " + rectangle);
		
		// logger.log("This node has a bounding box of " + rectangle.toString()
		// + " has parent? " + (parent != null) + "... child rect: " + childRectangle);

		if (parent != null && cache.getNode(parent) != null && goUp) {
			updateRectangleNDimensional(cache.getNode(parent));
		}
	}
	
	public void updateRectangleNDimensional(DepRTreeNode node) {
		logger.log(rectangle.getNumberDimensions() + "-Dimensional BRANCH UPDATE RECTANGLE:::: " + node.nodeId + " ... node.children: " + node.children + " node.parent: " + node.parent);
		
		IHyperRectangle childSum = node.rectangle;
		
		List<Integer> minimums = new ArrayList<Integer>();
		List<Integer> maximums = new ArrayList<Integer>();
		for (int i = 0; i < rectangle.getNumberDimensions(); i++) {
			minimums.add(null);
			maximums.add(null);
		}
		for (int i = 0; i < rectangle.getNumberDimensions(); i++) {
			minimums.set(i, node.rectangle.getDim1(i));
			maximums.set(i, node.rectangle.getDim2(i));
		}
		
		
		if (node.children != null) {
			DepRTreeNode firstChild = cache.getNode(node.getChildren().get(0));
			if (firstChild != null && firstChild.rectangle != null) {
				for (int i = 0; i < rectangle.getNumberDimensions(); i++) {
					minimums.set(i, firstChild.getRectangle().getDim1(i));
					maximums.set(i, firstChild.getRectangle().getDim2(i));

				}
			}
			
			
			if (firstChild != null) {
				childSum = firstChild.getRectangle();
			}
			
			for (String child : node.children) {
				
				DepRTreeNode childNode = cache.getNode(child);
				if (childNode != null && childNode.rectangle != null) {
					
					logger.log("childSum: " + childSum);
					IHyperRectangle childRectangle = cache.getNode(child).getRectangle();
					
					List<IHyperRectangle> temp = new ArrayList<IHyperRectangle>();
					temp.add(childSum);
					temp.add(childRectangle);
					childSum = HyperRectangleBase.sumRectanglesNDimensional(temp);
					
					for (int i = 0; i < rectangle.getNumberDimensions(); i++) {
						if (minimums.get(i) > childRectangle.getDim1(i)) {
							minimums.set(i, childRectangle.getDim1(i));
						}
						if (maximums.get(i) < childRectangle.getDim2(i)) {
							maximums.set(i, childRectangle.getDim2(i));
						}
					}
				}
			}
			
			
			
			node.rectangle = childSum;
			cache.updateNodeNDimensional(node.nodeId, null, null, null, node.rectangle.getJson().toJSONString());
		}
		
		if (node.parent != null && cache.getNode(node.parent) != null) {
			updateRectangleNDimensional(cache.getNode(node.parent));
		}
	
	}
	
	public void updateRectangle2D(DepRTreeNode node) {
		
		logger.log("BRANCH UPDATE RECTANGLE:::: " + node.nodeId + " ... node.children: " + node.children + " node.parent: " + node.parent);
		
		IHyperRectangle childSum = node.rectangle;
		
		int minX = node.rectangle.getDim1(0);
		int maxX = node.rectangle.getDim2(0);
		int minY = node.rectangle.getDim1(1);
		int maxY = node.rectangle.getDim2(1);
		
		if (node.children != null) {
			DepRTreeNode firstChild = cache.getNode(node.getChildren().get(0));
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
				
				DepRTreeNode childNode = cache.getNode(child);
				if (childNode != null && childNode.rectangle != null) {
					
					logger.log("childSum: " + childSum);
					IHyperRectangle childRectangle = cache.getNode(child).getRectangle();
					
					childSum = Rectangle2D.sumRectangles2D(childSum, childRectangle);
					
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
			cache.updateNode2D(node.nodeId, null, null, null, node.rectangle.getJson().toJSONString());
		}
		
		

//		node.rectangle.setX1(minX);
//		node.rectangle.setX2(maxX);
//		node.rectangle.setY1(minY);
//		node.rectangle.setY2(maxY);
		
		if (node.parent != null && cache.getNode(node.parent) != null) {
			updateRectangle2D(cache.getNode(node.parent));
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

	public void setItemsJson2D(String items) {
		
		JSONParser parser = new JSONParser();
		Object obj;
		this.locationItems = new ArrayList<ILocationItem>();
		
		try {
			if (items != null && !items.equals("") && !items.equals("delete")) {
				
				obj = parser.parse(items);
				JSONArray arr = (JSONArray) obj;
				for (int i = 0; i < arr.size(); i++) {
					JSONObject row = (JSONObject) arr.get(i);
					ILocationItem item = new LocationItem2D(Integer.parseInt(row.get("x").toString()), Integer.parseInt(row.get("y").toString()), row.get("type").toString());
					this.locationItems.add(item);
				}
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			logger.log("items: |" + items + "|");
			e.printStackTrace();
		}
		
	}
	
	public void setItemsJsonNDimensional(String items) {
		
		JSONParser parser = new JSONParser();
		Object obj;
		this.locationItems = new ArrayList<ILocationItem>();
		
		try {
			if (items != null && !items.equals("") && !items.equals("delete")) {
				
				obj = parser.parse(items);
				JSONArray arr = (JSONArray) obj;
				for (int i = 0; i < arr.size(); i++) {
					JSONObject row = (JSONObject) arr.get(i);
					
					ILocationItem item = new LocationItemND(rectangle.getNumberDimensions());
					
					for (int i2 = 0; i2 < rectangle.getNumberDimensions(); i2++) {
						
						switch (i2) {
						case 0: 
							item.setDim(i2, Integer.parseInt(row.get("x").toString()));
							break;
						case 1:
							item.setDim(i2, Integer.parseInt(row.get("y").toString()));
							break;
						case 2:
							item.setDim(i2, Integer.parseInt(row.get("z").toString()));
							break;
						default:
							item.setDim(i2, Integer.parseInt(row.get("" + i2).toString()));
							break;
						}
						
						item.setType(row.get("type").toString());
					}
					
					this.locationItems.add(item);
				}
			}
		} catch (ParseException e) {
			logger.log("items: |" + items + "|");
			e.printStackTrace();
		}
		
	}
	
}