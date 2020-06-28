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
import rtree.item.LocationItemBase;
import rtree.log.ILogger;
import rtree.rectangle.RectangleND;
import rtree.rectangle.IHyperRectangle;


/**
 * 
 * @author David Sergio
 *
 */
public abstract class RTreeNodeBase implements IRTreeNode {

	protected List<String> children;
	protected String parent;
	protected List<ILocationItem> locationItems;
	protected IHyperRectangle rectangle;
	protected String nodeId;
	protected IRTreeCache cache;
	protected ILogger logger;

	public RTreeNodeBase(String nodeId, String childrenStr, String parent, IRTreeCache cache, ILogger logger) {
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
		this.locationItems = new ArrayList<ILocationItem>();
		
		if (cache != null) {
			this.rectangle = new RectangleND(cache.getNumDimensions());
		}
	}
	
	public void setLocationItems(ArrayList<ILocationItem> locationItems) {
		this.locationItems = locationItems;
	}
	
	@Override
	public String getNodeId() {
		return nodeId;
	}
	
	@Override
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

	@Override
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
	
	@Override
	public JSONArray getItemsJSON() {
		
		return LocationItemBase.getItemsJSON(locationItems);
	}

	@Override
	public List<ILocationItem> getLocationItems() {
		return locationItems;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void addItem(ILocationItem locationItem) throws IOException {
		
		logger.log("CloudRTreeNode.addItem");
		System.out.println("Rectangle: " + this.rectangle);
		locationItems.add(locationItem);
		updateRectangle();
		
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
		
		cache.updateNode(this.nodeId, this.getChildrenJSON().toJSONString(), this.getParent(), itemsStr, rectStr);
		
		
	}
	
	public abstract void updateRectangle();
	public abstract void updateRectangle(boolean goUp);
	public abstract void updateRectangle(IRTreeNode node);
	

	@Override
	public int getNumberOfItems() {
		return locationItems.size();
	}

	@Override
	public boolean isLeafNode() {
		return children == null || children.size() == 0;
	}

	@Override
	public IHyperRectangle getRectangle() {
		return rectangle;
	}

	@Override
	public void setRectangle(IHyperRectangle rectangle) {
		this.rectangle = rectangle;
	}

	@Override
	public List<String> getChildren() {
		if (children != null) {
			return children;
		} else {
			children = new ArrayList<String>(); 
			return children;
		}
	}

	@Override
	public void setParent(String node) {
		this.parent = node;
	}

	@Override
	public List<ILocationItem> getPoints() {
		return locationItems;
	}

	@Override
	public String getParent() {
		return parent;
	}

	@Override
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

	
	
	
	
}